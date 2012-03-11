package org.lehirti.util;

import java.io.File;

import org.lehirti.res.images.ImageKey;
import org.lehirti.res.text.TextKey;

public class PathFinder {
  private static final String VERSION_FILE_LOCATION = "version";
  
  private static final String RES = "res";
  
  private static final File CORE_BASE_DIR = new File("core");
  private static final File MOD_BASE_DIR = new File("mod");
  
  private static final File CORE_RES_DIR = new File(PathFinder.CORE_BASE_DIR, RES);
  private static final File MOD_RES_DIR = new File(PathFinder.MOD_BASE_DIR, RES);
  
  public static final String PROXY_FILENAME_SUFFIX = ".proxy";
  
  public static File getCoreFile(final TextKey key) {
    return new File(PathFinder.CORE_RES_DIR, key.getClass().getName() + "." + key.name());
  }
  
  public static File getModFile(final TextKey key) {
    return new File(PathFinder.MOD_RES_DIR, key.getClass().getName() + "." + key.name());
  }
  
  public static File getCoreImageProxyFile(final ImageKey key) {
    return getDir(CORE_BASE_DIR, key);
  }
  
  public static File getModImageProxyFile(final ImageKey key) {
    return getDir(MOD_BASE_DIR, key);
  }
  
  private static File getDir(final File baseDir, final ImageKey key) {
    final File moduleDir = new File(baseDir, key.getClass().getName());
    final File keyDir = new File(moduleDir, key.name());
    return keyDir;
  }
  
  public static File getCoreImageFile(final String imageBaseName) {
    final File tmp = new File(CORE_RES_DIR, imageBaseName.substring(0, 2));
    return new File(tmp, imageBaseName);
  }
  
  public static File getModImageFile(final String imageBaseName) {
    final File tmp = new File(MOD_RES_DIR, imageBaseName.substring(0, 2));
    return new File(tmp, imageBaseName);
  }
  
  /**
   * @param coreFile
   *          file inside the core sub-directory
   * @return <p>
   *         if coreFile is inside the core sub-directory: corresponding file in the mod sub-directory
   *         </p>
   *         <p>
   *         else coreFile will be returned as-is
   *         </p>
   */
  public static File toModFile(final File coreFile) {
    return new File(coreFile.getAbsolutePath().replaceFirst(CORE_BASE_DIR.getAbsolutePath(),
        MOD_BASE_DIR.getAbsolutePath()));
  }
  
  /**
   * @param imageProxyFile
   * @return corresponding real file in core sub-directory
   */
  public static File imageProxyToCoreReal(final File imageProxyFile) {
    final int realImageFileNamelength = imageProxyFile.getName().length() - PROXY_FILENAME_SUFFIX.length();
    return getCoreImageFile(imageProxyFile.getName().substring(0, realImageFileNamelength));
  }
  
  public static File getProxyFile(final File baseDir, final String baseName) {
    return new File(baseDir, baseName + PathFinder.PROXY_FILENAME_SUFFIX);
  }
  
  public static String getLocationOfVerionsFileOnClasspath() {
    return VERSION_FILE_LOCATION;
  }
}
