package org.lehirti.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

public final class ExportCoreContent {
  private static final File CORE_DIR = new File("../AtrunGame/core");
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
    
    final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destZipFile));
    
    exportDir(dir, out);
    
    try {
      out.close();
    } catch (final ZipException zipEx) {
      zipEx.printStackTrace();
      destZipFile.delete();
    }
    System.out.println("FINISHED exporting " + dir.getAbsolutePath());
  }
  
  private static void exportDir(final File dir, final ZipOutputStream out) throws FileNotFoundException, IOException {
    final String[] entries = dir.list();
    final byte[] buffer = new byte[4096];
    int bytesRead;
    
    for (final String entrie : entries) {
      final File f = new File(dir, entrie);
      if (f.isDirectory()) {
        exportDir(f, out);
      } else {
        String zipEntryString = f.getPath();
        zipEntryString = zipEntryString.substring(zipEntryString.indexOf("core"));
        final ZipEntry entry = new ZipEntry(zipEntryString); // Make a ZipEntry
        System.out.println("Adding ZipEntry " + entry.getName());
        out.putNextEntry(entry); // Store entry
        final FileInputStream in = new FileInputStream(f); // Stream to read file
        while ((bytesRead = in.read(buffer)) != -1) {
          out.write(buffer, 0, bytesRead);
        }
        in.close();
      }
    }
  }
}
