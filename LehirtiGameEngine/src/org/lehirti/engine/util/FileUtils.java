package org.lehirti.engine.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
  
  public static boolean copyFile(final File sourceFile, final File destFile) {
    if (!destFile.getParentFile().exists()) {
      if (!destFile.getParentFile().mkdirs()) {
        LOGGER.error("Unable to create directory " + destFile.getParentFile().getAbsolutePath());
        return false;
      }
    }
    
    FileChannel source = null;
    FileChannel destination = null;
    
    try {
      source = new FileInputStream(sourceFile).getChannel();
      destination = new FileOutputStream(destFile).getChannel();
      destination.transferFrom(source, 0, source.size());
      return true;
    } catch (final IOException e) {
      LOGGER.error("Failed to copy " + sourceFile.getAbsolutePath() + " to " + destFile.getAbsolutePath(), e);
      return false;
    } finally {
      if (source != null) {
        try {
          source.close();
        } catch (final IOException e) {
          LOGGER.error("Error closing source " + source.toString(), e);
        }
      }
      if (destination != null) {
        try {
          destination.close();
        } catch (final IOException e) {
          LOGGER.error("Error closing destination " + source.toString(), e);
        }
      }
    }
  }
  
  private static boolean copyFile(final InputStream srcInputStream, final File destFile) {
    if (!destFile.getParentFile().exists()) {
      if (!destFile.getParentFile().mkdirs()) {
        LOGGER.error("Unable to create directory " + destFile.getParentFile().getAbsolutePath());
        return false;
      }
    }
    
    try {
      return copyStream(srcInputStream, new BufferedOutputStream(new FileOutputStream(destFile)));
    } catch (final FileNotFoundException e) {
      LOGGER.error("Unable to create " + destFile.getAbsolutePath(), e);
      return false;
    }
  }
  
  public static final boolean copyStream(final InputStream in, final OutputStream out) {
    final byte[] buffer = new byte[4096];
    int len;
    try {
      while ((len = in.read(buffer)) >= 0) {
        out.write(buffer, 0, len);
      }
    } catch (final IOException e) {
      LOGGER.error("Failed to tranfer data between streams", e);
      return false;
    } finally {
      try {
        in.close();
      } catch (final IOException e) {
        LOGGER.error("Failed to close input stream", e);
      }
      try {
        out.close();
      } catch (final IOException e) {
        LOGGER.error("Failed to close output stream", e);
      }
    }
    return true;
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
      return new String(buffer);
    } catch (final FileNotFoundException e) {
      LOGGER.error("Cannot read file content of file: " + file.getAbsolutePath() + ": File not found.", e);
    } catch (final IOException e) {
      LOGGER.error("Cannot read file content of file: " + file.getAbsolutePath() + ": IOException.", e);
    }
    return "Failed to read file content of file " + file.getAbsolutePath() + " see log file.";
  }
  
  public static void writeContentToFile(final File file, final String value) {
    if (!file.getParentFile().exists()) {
      if (!file.getParentFile().mkdirs()) {
        LOGGER.error("Cannot write to file " + file.getAbsolutePath() + " because parent dir could not be created.");
        return;
      }
    }
    
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(file));
      writer.write(value);
    } catch (final IOException e) {
      LOGGER.error("Failed to write to file " + file.getAbsolutePath(), e);
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (final IOException e) {
        LOGGER.error("Failed to close writer for file " + file.getAbsolutePath(), e);
      }
    }
  }
  
  public static boolean readPropsFromFile(final Properties properties, final File file) {
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      properties.load(fis);
      return true;
    } catch (final FileNotFoundException e) {
      LOGGER.error("Properties file " + file.getAbsolutePath() + " not found.", e);
    } catch (final IOException e) {
      LOGGER.error("IOException reading properties file " + file.getAbsolutePath(), e);
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (final IOException e) {
          LOGGER.error("Failed to close file input stream for " + file.getAbsolutePath(), e);
        }
      }
    }
    return false;
  }
  
  public static boolean writePropsToFile(final Properties properties, final File file, final String comments) {
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file);
      properties.store(fos, comments);
      return true;
    } catch (final FileNotFoundException e) {
      LOGGER.error("Properties file " + file.getAbsolutePath() + " not found for writing.", e);
    } catch (final IOException e) {
      LOGGER.error("IOException writing properties file " + file.getAbsolutePath(), e);
    } finally {
      if (fos != null) {
        try {
          fos.close();
        } catch (final IOException e) {
          LOGGER.error("Error closing file output stream for " + file.getAbsolutePath(), e);
        }
      }
    }
    return false;
  }
  
  public static boolean deleteRecursive(final File f) {
    if (f.isDirectory()) {
      for (final File c : f.listFiles()) {
        if (!deleteRecursive(c)) {
          return false;
        }
      }
    }
    if (!f.delete()) {
      return false;
    }
    return true;
  }
  
  public static boolean unpack(final ZipEntry elementToUnpack, final ZipFile zipFile) {
    if (elementToUnpack.isDirectory()) {
      return true;
    }
    final String nameOfElementToUnpack = elementToUnpack.getName();
    final File destFile = new File(nameOfElementToUnpack);
    final File parent = destFile.getParentFile();
    if (!parent.exists()) {
      if (!parent.mkdirs()) {
        LOGGER.error("Failed to create parent directory for " + nameOfElementToUnpack);
        return false;
      }
    }
    try {
      final InputStream zipFileEntryContentStream = zipFile.getInputStream(elementToUnpack);
      final boolean success = copyFile(zipFileEntryContentStream, destFile);
      if (!success) {
        LOGGER.error("Failed to unpack {} to {}", elementToUnpack.getName(), destFile.getAbsolutePath());
      }
      return success;
    } catch (final IOException e) {
      LOGGER.error("Failed to read " + nameOfElementToUnpack + " from " + zipFile.getName(), e);
      return false;
    }
  }
}
