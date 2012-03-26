package org.lehirti.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(ContentUtils.class);
  
  public static enum CheckResult {
    OK,
    NEEDS_UPDATE,
    MISSING;
  }
  
  public static CheckResult check(final String contentKey, final int requiredVersion) {
    final File manifestFile = PathFinder.getManifest(contentKey, requiredVersion);
    if (manifestFile.exists()) {
      return CheckResult.OK;
    }
    final File contentZipFile = PathFinder.getContentZipFile(contentKey, requiredVersion);
    if (contentZipFile.exists()) {
      return CheckResult.NEEDS_UPDATE;
    } else {
      return CheckResult.MISSING;
    }
  }
  
  public static void rebuild(final String contentKey, final Integer requiredVersion) {
    LOGGER.info("START: Rebuilding {}-{}", contentKey, requiredVersion);
    final File coreDir = PathFinder.getCoreContentDir(contentKey);
    if (!FileUtils.deleteRecursive(coreDir)) {
      LOGGER.error("Failed to delete old " + coreDir.getAbsolutePath());
    }
    
    final SortedMap<Integer, ZipFile> contentZipFiles = getContentZipFiles(contentKey);
    final ZipFile reqVerZipFile = contentZipFiles.remove(requiredVersion);
    
    boolean anErrorHasOccurred = false;
    
    // unpack newest content version zip file
    final Enumeration<? extends ZipEntry> entries = reqVerZipFile.entries();
    while (entries.hasMoreElements()) {
      final ZipEntry nextElement = entries.nextElement();
      if (!FileUtils.unpack(nextElement, reqVerZipFile)) {
        anErrorHasOccurred = true;
        LOGGER.error("ERROR: Rebuilding {}-{}: Failed to unpack " + nextElement.getName(), contentKey, requiredVersion);
      }
    }
    
    try {
      reqVerZipFile.close();
    } catch (final IOException e) {
      LOGGER.warn("Failed to close " + reqVerZipFile.getName(), e);
    }
    
    // now, read manifest and fill the gaps with older content files
    final File manifestFile = PathFinder.getManifest(contentKey, requiredVersion.intValue());
    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(manifestFile));
    } catch (final FileNotFoundException e) {
      LOGGER.error("Manifest file " + manifestFile.getAbsolutePath() + " not found");
      return;
    }
    
    try {
      String filePath;
      while ((filePath = reader.readLine()) != null) {
        final boolean success = extractFrom(filePath, contentZipFiles);
        if (!success) {
          anErrorHasOccurred = true;
          LOGGER.error("Failed to extract " + filePath + " from older content zip files");
        }
      }
    } catch (final IOException e) {
      anErrorHasOccurred = true;
      LOGGER.error("Error reading manifest file " + manifestFile.getAbsolutePath());
    }
    
    for (final ZipFile zipFile : contentZipFiles.values()) {
      try {
        zipFile.close();
      } catch (final IOException e) {
        LOGGER.warn("Failed to close " + zipFile.getName(), e);
      }
    }
    
    if (anErrorHasOccurred) {
      if (!manifestFile.delete()) {
        LOGGER.error("Failed to delete manifest file " + manifestFile.getAbsolutePath()
            + " after rebuilt has finished with errors.");
      }
    }
    
    LOGGER.info("FINISHED: Rebuilding {}-{}", contentKey, requiredVersion);
  }
  
  private static boolean extractFrom(final String filePath, final SortedMap<Integer, ZipFile> contentZipFiles) {
    for (final ZipFile zipFile : contentZipFiles.values()) {
      final ZipEntry entry = zipFile.getEntry(filePath);
      if (entry != null) {
        // found
        return FileUtils.unpack(entry, zipFile);
      }
    }
    
    return false; // not found
  }
  
  /**
   * @param contentKey
   * @return all available zip files for contentKey; newest first
   */
  private static SortedMap<Integer, ZipFile> getContentZipFiles(final String contentKey) {
    final SortedMap<Integer, ZipFile> map = new TreeMap<Integer, ZipFile>(Collections.reverseOrder());
    final File rootDir = new File("");
    final File[] contentZipFiles = rootDir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(final File dir, final String name) {
        return name.startsWith(contentKey) && name.endsWith(".zip");
      }
    });
    for (final File zipFile : contentZipFiles) {
      final String name = zipFile.getName();
      final String versionNumberString = name.substring(contentKey.length() + 1, name.length() - ".zip".length());
      final Integer versionNumber = Integer.valueOf(versionNumberString);
      try {
        map.put(versionNumber, new ZipFile(zipFile));
      } catch (final ZipException e) {
        LOGGER.error("Unable to read zip file " + zipFile.getAbsolutePath(), e);
      } catch (final IOException e) {
        LOGGER.error("Unable to read zip file " + zipFile.getAbsolutePath(), e);
      }
    }
    return map;
  }
}
