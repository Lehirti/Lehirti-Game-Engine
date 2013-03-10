package lge.util;

/*
 * ClassFinder.java
 */

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This utility class was based originally on <a href="private.php?do=newpm&u=47838">Daniel Le Berre</a>'s
 * <code>RTSI</code> class. This class can be called in different modes, but the principal use is to determine what
 * subclasses/implementations of a given class/interface exist in the current runtime environment.
 * 
 * @author Daniel Le Berre, Elliott Wade
 */
public class ClassFinder {
  private Class<?> searchClass = null;
  private Map<URL, String> classpathLocations = new HashMap<>();
  private Map<Class<?>, URL> results = new HashMap<>();
  private List<Throwable> errors = new ArrayList<>();
  
  public ClassFinder() {
    refreshLocations();
  }
  
  /**
   * Rescan the classpath, cacheing all possible file locations.
   */
  public synchronized final void refreshLocations() {
    this.classpathLocations = getClasspathLocations();
  }
  
  /**
   * @param fqcn
   *          Name of superclass/interface on which to search
   */
  public synchronized final Vector<Class<?>> findSubclasses(final String fqcn) {
    this.searchClass = null;
    this.errors = new ArrayList<>();
    this.results = new TreeMap<>(CLASS_COMPARATOR);
    
    //
    // filter malformed FQCN
    //
    if (fqcn.startsWith(".") || fqcn.endsWith(".")) {
      return new Vector<>();
    }
    
    //
    // Determine search class from fqcn
    //
    try {
      this.searchClass = Class.forName(fqcn);
    } catch (final ClassNotFoundException ex) {
      // if class not found, let empty vector return...
      this.errors.add(ex);
      return new Vector<>();
    }
    
    return findSubclasses(this.searchClass, this.classpathLocations);
  }
  
  public synchronized final List<Throwable> getErrors() {
    return new ArrayList<>(this.errors);
  }
  
  /**
   * The result of the last search is cached in this object, along with the URL that corresponds to each class returned.
   * This method may be called to query the cache for the location at which the given class was found. <code>null</code>
   * will be returned if the given class was not found during the last search, or if the result cache has been cleared.
   */
  public synchronized final URL getLocationOf(final Class<?> cls) {
    if (this.results != null) {
      return this.results.get(cls);
    } else {
      return null;
    }
  }
  
  /**
   * Determine every URL location defined by the current classpath, and it's associated package name.
   */
  public synchronized final Map<URL, String> getClasspathLocations() {
    final Map<URL, String> map = new TreeMap<>(URL_COMPARATOR);
    File file = null;
    
    final String pathSep = System.getProperty("path.separator");
    final String classpath = System.getProperty("java.class.path");
    // System.out.println("classpath=" + classpath);
    
    final StringTokenizer st = new StringTokenizer(classpath, pathSep);
    while (st.hasMoreTokens()) {
      final String path = st.nextToken();
      file = new File(path);
      include(null, file, map);
    }
    
    final Iterator<URL> it = map.keySet().iterator();
    while (it.hasNext()) {
      final URL url = it.next();
      // System.out.println(url + "-->" + map.get(url));
    }
    
    return map;
  }
  
  private final static FileFilter DIRECTORIES_ONLY = new FileFilter() {
    public boolean accept(final File f) {
      if (f.exists() && f.isDirectory()) {
        return true;
      } else {
        return false;
      }
    }
  };
  
  private final static Comparator<URL> URL_COMPARATOR = new Comparator<URL>() {
    public int compare(final URL u1, final URL u2) {
      return String.valueOf(u1).compareTo(String.valueOf(u2));
    }
  };
  
  private final static Comparator<Class<?>> CLASS_COMPARATOR = new Comparator<Class<?>>() {
    public int compare(final Class<?> c1, final Class<?> c2) {
      return String.valueOf(c1).compareTo(String.valueOf(c2));
    }
  };
  
  private final void include(String name, final File file, final Map<URL, String> map) {
    if (!file.exists()) {
      return;
    }
    if (!file.isDirectory()) {
      // could be a JAR file
      includeJar(file, map);
      return;
    }
    
    if (name == null) {
      name = "";
    } else {
      name += ".";
    }
    
    // add subpackages
    final File[] dirs = file.listFiles(DIRECTORIES_ONLY);
    for (final File dir : dirs) {
      try {
        // add the present package
        map.put(new URL("file://" + dir.getCanonicalPath()), name + dir.getName());
      } catch (final IOException ioe) {
        return;
      }
      
      include(name + dir.getName(), dir, map);
    }
  }
  
  private final static void includeJar(final File file, final Map<URL, String> map) {
    if (file.isDirectory()) {
      return;
    }
    
    URL jarURL = null;
    JarFile jar = null;
    try {
      String canonicalPath = file.getCanonicalPath();
      if (canonicalPath.startsWith("/")) {
        canonicalPath = canonicalPath.substring(1);
      }
      jarURL = new URL("file:/" + canonicalPath);
      jarURL = new URL("jar:" + jarURL.toExternalForm() + "!/");
      final JarURLConnection conn = (JarURLConnection) jarURL.openConnection();
      jar = conn.getJarFile();
    } catch (final Exception e) {
      // not a JAR or disk I/O error
      // either way, just skip
      return;
    }
    
    if (jar == null) {
      return;
    }
    
    // include the jar's "default" package (i.e. jar's root)
    map.put(jarURL, "");
    
    final Enumeration<JarEntry> e = jar.entries();
    while (e.hasMoreElements()) {
      final JarEntry entry = e.nextElement();
      
      if (entry.isDirectory()) {
        if (entry.getName().toUpperCase().equals("META-INF/")) {
          continue;
        }
        
        try {
          map.put(new URL(jarURL.toExternalForm() + entry.getName()), packageNameFor(entry));
        } catch (final MalformedURLException murl) {
          // whacky entry?
          continue;
        }
      }
    }
  }
  
  private static String packageNameFor(final JarEntry entry) {
    if (entry == null) {
      return "";
    }
    String s = entry.getName();
    if (s == null) {
      return "";
    }
    if (s.length() == 0) {
      return s;
    }
    if (s.startsWith("/")) {
      s = s.substring(1, s.length());
    }
    if (s.endsWith("/")) {
      s = s.substring(0, s.length() - 1);
    }
    return s.replace('/', '.');
  }
  
  private final void includeResourceLocations(final String packageName, final Map<URL, String> map) {
    try {
      final Enumeration<URL> resourceLocations = ClassFinder.class.getClassLoader().getResources(
          getPackagePath(packageName));
      
      while (resourceLocations.hasMoreElements()) {
        map.put(resourceLocations.nextElement(), packageName);
      }
    } catch (final Exception e) {
      // well, we tried
      this.errors.add(e);
      return;
    }
  }
  
  private final Vector<Class<?>> findSubclasses(final Class<?> superClass, final Map<URL, String> locations) {
    final Vector<Class<?>> v = new Vector<>();
    
    Vector<Class<?>> w = null; // new Vector<Class<?>> ();
    
    // Package [] packages = Package.getPackages ();
    // for (int i=0;i<packages.length;i++)
    // {
    // System.out.println ("package: " + packages[i]);
    // }
    
    for (final Map.Entry<URL, String> entry : locations.entrySet()) {
      final URL url = entry.getKey();
      // System.out.println (url + "-->" + locations.get (url));
      
      w = findSubclasses(url, entry.getValue(), superClass);
      if (w != null && (w.size() > 0)) {
        v.addAll(w);
      }
    }
    
    return v;
  }
  
  private final Vector<Class<?>> findSubclasses(final URL location, final String packageName, final Class<?> superClass) {
    // System.out.println ("looking in package:" + packageName);
    // System.out.println ("looking for  class:" + superClass);
    
    synchronized (this.results) {
      
      // hash guarantees unique names...
      final Map<Class<?>, URL> thisResult = new TreeMap<>(CLASS_COMPARATOR);
      final Vector<Class<?>> v = new Vector<>(); // ...but return a
      // vector
      
      // TODO: double-check for null search class
      final String fqcn = this.searchClass.getName();
      
      final List<URL> knownLocations = new ArrayList<>();
      knownLocations.add(location);
      // TODO: add getResourceLocations() to this list
      
      // iterate matching package locations...
      for (int loc = 0; loc < knownLocations.size(); loc++) {
        final URL url = knownLocations.get(loc);
        
        // Get a File object for the package
        final File directory = new File(url.getFile());
        
        // System.out.println ("\tlooking in " + directory);
        
        if (directory.exists()) {
          // Get the list of the files contained in the package
          final String[] files = directory.list();
          for (final String file : files) {
            // we are only interested in .class files
            if (file.endsWith(".class")) {
              // removes the .class extension
              final String classname = file.substring(0, file.length() - 6);
              
              // System.out.println ("\t\tchecking file " + classname);
              
              try {
                final Class<?> c = Class.forName(packageName + "." + classname);
                if (superClass.isAssignableFrom(c) && !fqcn.equals(packageName + "." + classname)) {
                  thisResult.put(c, url);
                }
              } catch (final ClassNotFoundException cnfex) {
                this.errors.add(cnfex);
                // System.err.println(cnfex);
              } catch (final Exception ex) {
                this.errors.add(ex);
                // System.err.println (ex);
              }
            }
          }
        } else {
          try {
            // It does not work with the filesystem: we must
            // be in the case of a package contained in a jar file.
            final JarURLConnection conn = (JarURLConnection) url.openConnection();
            // String starts = conn.getEntryName();
            final JarFile jarFile = conn.getJarFile();
            
            // System.out.println ("starts=" + starts);
            // System.out.println ("JarFile=" + jarFile);
            
            final Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
              final JarEntry entry = e.nextElement();
              final String entryname = entry.getName();
              
              // System.out.println ("\tconsidering entry: " + entryname);
              
              if (!entry.isDirectory() && entryname.endsWith(".class")) {
                String classname = entryname.substring(0, entryname.length() - 6);
                if (classname.startsWith("/")) {
                  classname = classname.substring(1);
                }
                classname = classname.replace('/', '.');
                
                // System.out.println ("\t\ttesting classname: " + classname);
                
                try {
                  // TODO: verify this block
                  final Class c = Class.forName(classname);
                  
                  if (superClass.isAssignableFrom(c) && !fqcn.equals(classname)) {
                    thisResult.put(c, url);
                  }
                } catch (final ClassNotFoundException cnfex) {
                  // that's strange since we're scanning
                  // the same classpath the classloader's
                  // using... oh, well
                  this.errors.add(cnfex);
                } catch (final NoClassDefFoundError ncdfe) {
                  // dependency problem... class is
                  // unusable anyway, so just ignore it
                  this.errors.add(ncdfe);
                } catch (final UnsatisfiedLinkError ule) {
                  // another dependency problem... class is
                  // unusable anyway, so just ignore it
                  this.errors.add(ule);
                } catch (final Exception exception) {
                  // unexpected problem
                  // System.err.println (ex);
                  this.errors.add(exception);
                } catch (final Error error) {
                  // lots of things could go wrong
                  // that we'll just ignore since
                  // they're so rare...
                  this.errors.add(error);
                }
              }
            }
          } catch (final IOException ioex) {
            // System.err.println(ioex);
            this.errors.add(ioex);
          }
        }
      } // while
      
      // System.out.println ("results = " + thisResult);
      
      this.results.putAll(thisResult);
      
      final Iterator<Class<?>> it = thisResult.keySet().iterator();
      while (it.hasNext()) {
        v.add(it.next());
      }
      return v;
      
    } // synch results
  }
  
  private final static String getPackagePath(final String packageName) {
    // Translate the package name into an "absolute" path
    String path = packageName;
    if (!path.startsWith("/")) {
      path = "/" + path;
    }
    path = path.replace('.', '/');
    
    // ending with "/" indicates a directory to the classloader
    if (!path.endsWith("/")) {
      path += "/";
    }
    
    // for actual classloader interface (NOT Class.getResource() which
    // hacks up the request string!) a resource beginning with a "/"
    // will never be found!!! (unless it's at the root, maybe?)
    if (path.startsWith("/")) {
      path = path.substring(1, path.length());
    }
    
    // System.out.println ("package path=" + path);
    
    return path;
  }
}
