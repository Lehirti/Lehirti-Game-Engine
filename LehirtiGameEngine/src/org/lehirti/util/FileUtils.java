package org.lehirti.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {
  public static void copyFileTODO(final File sourceFile, final File destFile) {
    try {
      if (!destFile.getParentFile().exists()) {
        destFile.getParentFile().mkdirs();
      }
      copyFile(sourceFile, destFile);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public static void copyFile(final File sourceFile, final File destFile) throws IOException {
    if (!destFile.exists()) {
      destFile.createNewFile();
    }
    
    FileChannel source = null;
    FileChannel destination = null;
    
    try {
      source = new FileInputStream(sourceFile).getChannel();
      destination = new FileOutputStream(destFile).getChannel();
      destination.transferFrom(source, 0, source.size());
    } finally {
      if (source != null) {
        source.close();
      }
      if (destination != null) {
        destination.close();
      }
    }
  }
  
  public static String readContentAsString(final File file) {
    char[] buffer = null;
    
    try {
      final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
      
      buffer = new char[(int) file.length()];
      
      int i = 0;
      int c = bufferedReader.read();
      
      while (c != -1) {
        buffer[i++] = (char) c;
        c = bufferedReader.read();
      }
    } catch (final FileNotFoundException e) {
      // TODO
    } catch (final IOException e) {
      // TODO
    }
    
    return new String(buffer);
  }
  
  public static void writeContentToFile(final File file, final String value) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(file));
      writer.write(value);
    } catch (final IOException e) {
      // TODO
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (final IOException e) {
        // TODO
      }
    }
  }
}
