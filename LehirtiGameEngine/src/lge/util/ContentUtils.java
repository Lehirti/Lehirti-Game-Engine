package lge.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import lge.gui.EngineMain;
import lge.res.ResourceCache;
import lge.res.ResourceState;
import lge.res.images.ImageKey;
import lge.res.images.ImageWrapper;
import lge.res.text.TextKey;
import lge.res.text.TextWrapper;
import lge.util.ClassFinder.SuperClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(ContentUtils.class);
  
  public static enum CheckResult {
    DEV_ENVIRONMENT,
    OK,
    NEEDS_UPDATE,
    MISSING;
  }
  
  public static CheckResult check(final String contentKey, final int requiredVersion) {
    if (EngineMain.IS_DEVELOPMENT_VERSION) {
      return CheckResult.DEV_ENVIRONMENT;
    }
    
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
    if (coreDir.exists() && !FileUtils.deleteRecursive(coreDir)) {
      LOGGER.error("Failed to delete old " + coreDir.getAbsolutePath());
    }
    
    final SortedMap<Integer, ZipFile> contentZipFiles = getContentZipFiles(contentKey, new File("."));
    final ZipFile reqVerZipFile = contentZipFiles.remove(requiredVersion);
    
    boolean anErrorHasOccurred = false;
    
    final Set<String> requiredResources = new LinkedHashSet<>();
    final Set<String> foundResources = new LinkedHashSet<>();
    
    // unpack newest content version zip file
    final Enumeration<? extends ZipEntry> entries = reqVerZipFile.entries();
    while (entries.hasMoreElements()) {
      final ZipEntry nextElement = entries.nextElement();
      if (!FileUtils.unpack(nextElement, reqVerZipFile)) {
        anErrorHasOccurred = true;
        LOGGER.error("ERROR: Rebuilding {}-{}: Failed to unpack " + nextElement.getName(), contentKey, requiredVersion);
      } else {
        if (nextElement.getName().endsWith(PathFinder.PROXY_FILENAME_SUFFIX)) {
          final File imageFile = PathFinder.imageProxyToCoreReal(new File(nextElement.getName()));
          requiredResources.add(imageFile.getPath().replaceAll("\\\\", "/"));
        } else if (nextElement.getName().indexOf("res") != -1) {
          foundResources.add(nextElement.getName());
        }
      }
    }
    
    try {
      reqVerZipFile.close();
    } catch (final IOException e) {
      LOGGER.warn("Failed to close " + reqVerZipFile.getName(), e);
    }
    
    requiredResources.removeAll(foundResources);
    // now, we need to read all the resources that are not in the newest content pack from older content packs
    
    for (final String requiredPath : requiredResources) {
      final boolean success = extractFrom(requiredPath, contentZipFiles);
      if (!success) {
        anErrorHasOccurred = true;
        LOGGER.error("Failed to extract " + requiredPath + " from older content zip files");
      }
    }
    
    for (final ZipFile zipFile : contentZipFiles.values()) {
      try {
        zipFile.close();
      } catch (final IOException e) {
        LOGGER.warn("Failed to close " + zipFile.getName(), e);
      }
    }
    
    if (anErrorHasOccurred) {
      final File manifestFile = PathFinder.getManifest(contentKey, requiredVersion.intValue());
      if (!manifestFile.delete()) {
        LOGGER.error("Failed to delete manifest file " + manifestFile.getAbsolutePath()
            + " after rebuilt has finished with errors.");
      }
      LOGGER.info("FINISHED with ERRORS: Rebuilding {}-{}", contentKey, requiredVersion);
    } else {
      LOGGER.info("FINISHED: Rebuilding {}-{}", contentKey, requiredVersion);
    }
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
   * @param rootDir
   * @return all available zip files for contentKey; newest first
   */
  public static SortedMap<Integer, ZipFile> getContentZipFiles(final String contentKey, final File rootDir) {
    LOGGER.debug("Getting zip files for {}", contentKey);
    final SortedMap<Integer, ZipFile> map = new TreeMap<>(Collections.reverseOrder());
    LOGGER.debug("Looking for zip files in {}", rootDir.getAbsolutePath());
    final File[] contentZipFiles = rootDir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(final File dir, final String name) {
        LOGGER.debug("Checking name {}", name);
        return name.startsWith(contentKey) && name.endsWith(".zip");
      }
    });
    for (final File zipFile : contentZipFiles) {
      final String name = zipFile.getName();
      final String versionNumberString = name.substring(contentKey.length() + 1, name.length() - ".zip".length());
      final Integer versionNumber;
      if (versionNumberString.indexOf("-") != -1) {
        final String[] versionsFromTo = versionNumberString.split("-");
        versionNumber = Integer.valueOf(versionsFromTo[versionsFromTo.length - 1]);
      } else {
        versionNumber = Integer.valueOf(versionNumberString);
      }
      try {
        map.put(versionNumber, new ZipFile(zipFile));
        LOGGER.debug("Zip file {} has version number {}", name, versionNumber);
      } catch (final ZipException e) {
        LOGGER.error("Unable to read zip file " + zipFile.getAbsolutePath(), e);
      } catch (final IOException e) {
        LOGGER.error("Unable to read zip file " + zipFile.getAbsolutePath(), e);
      }
    }
    return map;
  }
  
  public static List<ImageWrapper> getImageWrappers(final boolean withoutImagesOnly) {
    final List<ImageWrapper> imageWrapperList = new ArrayList<>(10000);
    final List<Class<?>> imageEnums = ClassFinder.getSubclassesInFullClasspathStatic(SuperClass.IMAGE_KEY);
    System.out.println("Read all ImageKey classes");
    for (final Class<?> imageEnum : imageEnums) {
      final ImageKey[] imageKeys = (ImageKey[]) imageEnum.getEnumConstants();
      if (imageKeys != null) {
        for (final ImageKey key : imageKeys) {
          final ImageWrapper imageWrapper = ResourceCache.get(key);
          if (withoutImagesOnly && imageWrapper.getResourceState() != ResourceState.MISSING) {
            continue;
          }
          imageWrapperList.add(imageWrapper);
        }
      }
    }
    return imageWrapperList;
  }
  
  public static List<TextWrapper> getTextWrappers(final boolean withoutImagesOnly) {
    final List<TextWrapper> textWrapperList = new ArrayList<>(10000);
    final List<Class<?>> textEnums = ClassFinder.getSubclassesInFullClasspathStatic(SuperClass.TEXT_KEY);
    for (final Class<?> textEnum : textEnums) {
      final TextKey[] textKeys = (TextKey[]) textEnum.getEnumConstants();
      if (textKeys != null) {
        for (final TextKey key : textKeys) {
          final TextWrapper textWrapper = ResourceCache.get(key);
          if (withoutImagesOnly && textWrapper.getResourceState() != ResourceState.MISSING) {
            continue;
          }
          textWrapperList.add(textWrapper);
        }
      }
    }
    return textWrapperList;
  }
}
