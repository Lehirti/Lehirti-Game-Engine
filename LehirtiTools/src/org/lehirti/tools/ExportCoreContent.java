package org.lehirti.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.lehirti.util.ContentUtils;
import org.lehirti.util.PathFinder;

public final class ExportCoreContent {
  private static final File ROOT_DIR = new File("../AtrunGame");
  private static final File CORE_DIR = new File(ROOT_DIR, "core");
  private static final File DEST_DIR = new File("../../lehirti_release");
  
  public static void main(final String[] args) throws IOException {
    System.out.println("START " + ExportCoreContent.class.getSimpleName());
    for (final File dir : CORE_DIR.listFiles()) {
      if (dir.getName().equals("res") || !dir.isDirectory()) {
        System.out.println("Skipping " + dir.getAbsolutePath());
        continue;
      }
      export(dir);
    }
    
    System.out.println("FINISHED " + ExportCoreContent.class.getSimpleName());
  }
  
  private static void export(final File dir) throws IOException {
    System.out.println("START exporting " + dir.getAbsolutePath());
    
    final String name = dir.getName();
    final File destZipFile = new File(DEST_DIR, name + "-1.zip"); // TODO -1
    System.out.println("Target " + destZipFile.getAbsolutePath());
    
    final SortedMap<Integer, ZipFile> contentZipFiles = ContentUtils.getContentZipFiles(name, DEST_DIR);
    
    // remove "not older" zip files (should be at most one; with the same version number as the one being built
    final Iterator<Map.Entry<Integer, ZipFile>> itr = contentZipFiles.entrySet().iterator();
    while (itr.hasNext()) {
      final Entry<Integer, ZipFile> entry = itr.next();
      if (entry.getKey().intValue() >= 1) { // TODO 1
        entry.getValue().close();
        itr.remove();
      }
    }
    
    final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destZipFile));
    
    // Make manifest
    final ZipEntry entry = new ZipEntry("core/manifest/" + name + "-1"); // TODO -1
    out.putNextEntry(entry);
    
    exportDir(dir, out, contentZipFiles);
    
    out.close();
    
    System.out.println("FINISHED exporting " + dir.getAbsolutePath());
  }
  
  private static void exportDir(final File dir, final ZipOutputStream out,
      final SortedMap<Integer, ZipFile> contentZipFiles) throws FileNotFoundException, IOException {
    final String[] entries = dir.list();
    final byte[] buffer = new byte[4096];
    int bytesRead;
    
    for (final String e : entries) {
      final File f = new File(dir, e);
      if (f.isDirectory()) {
        exportDir(f, out, contentZipFiles);
      } else {
        String zipEntryString = f.getPath();
        zipEntryString = zipEntryString.substring(zipEntryString.indexOf("core"));
        final ZipEntry entry = new ZipEntry(zipEntryString);
        System.out.println("Adding ZipEntry " + entry.getName());
        out.putNextEntry(entry);
        FileInputStream in = new FileInputStream(f);
        while ((bytesRead = in.read(buffer)) != -1) {
          out.write(buffer, 0, bytesRead);
        }
        in.close();
        if (f.getName().endsWith(PathFinder.PROXY_FILENAME_SUFFIX)) {
          final String imageBaseName = f.getName().substring(0,
              f.getName().length() - PathFinder.PROXY_FILENAME_SUFFIX.length());
          final File imageFile = PathFinder.getCoreImageFile(imageBaseName);
          final ZipEntry imageZipEntry = new ZipEntry(imageFile.getPath());
          if (!imageIsContainedInOlderZipFile(imageZipEntry, contentZipFiles)) {
            final File correctedPathImageFile = new File(ROOT_DIR, imageFile.getPath());
            System.out.println("Adding ZipEntry " + imageZipEntry.getName());
            out.putNextEntry(imageZipEntry);
            in = new FileInputStream(correctedPathImageFile);
            while ((bytesRead = in.read(buffer)) != -1) {
              out.write(buffer, 0, bytesRead);
            }
            in.close();
          }
        }
      }
    }
  }
  
  private static boolean imageIsContainedInOlderZipFile(final ZipEntry imageFile,
      final SortedMap<Integer, ZipFile> contentZipFiles) {
    for (final ZipFile zipFile : contentZipFiles.values()) {
      if (zipFile.getEntry(imageFile.getName()) != null) {
        return true;
      }
    }
    return false;
  }
}
