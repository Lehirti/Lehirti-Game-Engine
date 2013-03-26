package org.lehirti.tools;

import java.io.File;
import java.util.Vector;

import lge.res.ResourceCache;
import lge.res.ResourceState;
import lge.res.images.ImageKey;
import lge.res.images.ImageWrapper;
import lge.res.text.TextKey;
import lge.res.text.TextWrapper;
import lge.util.ClassFinder;
import lge.util.ContentUtils;
import lge.util.FileUtils;
import lge.util.PathFinder;
import main.C;

public final class ContentChecker {
  public static void main(final String[] args) {
    final StringBuilder websiteMissingeImageList = new StringBuilder();
    
    for (final C content : C.values()) {
      PathFinder.registerContentDir(content.name());
    }
    
    int iCore = 0;
    int iMod = 0;
    for (final ImageWrapper imageWrapper : ContentUtils.getImageWrappers(false)) {
      if (imageWrapper.getResourceState() == ResourceState.MISSING) {
        websiteMissingeImageList.append("<li>");
        websiteMissingeImageList.append(imageWrapper.toString());
        websiteMissingeImageList.append("</li>\n");
        System.out.println(ResourceState.MISSING + " image: " + imageWrapper.toString());
      } else {
        iCore += imageWrapper.getNrOfCoreImages();
        iMod += imageWrapper.getNrOfModImages();
      }
    }
    
    File missing_images = new File(".").getAbsoluteFile();
    while (!containsLuckySurvivorDir(missing_images.listFiles())) {
      missing_images = missing_images.getParentFile();
    }
    missing_images = missing_images.getParentFile();
    missing_images = new File(missing_images, "luckysurvivor");
    missing_images = new File(missing_images, "missing_images.html");
    if (missing_images.exists()) {
      final String missingImagesTemplate = FileUtils.readContentAsString(missing_images);
      final int indexOf = missingImagesTemplate.indexOf("MIL");
      final String missingImagesContent = missingImagesTemplate.substring(0, indexOf)
          + websiteMissingeImageList.toString() + missingImagesTemplate.substring(indexOf + 3);
      FileUtils.writeContentToFile(missing_images, missingImagesContent);
    }
    
    System.out.println();
    
    int tCore = 0;
    int tMod = 0;
    for (final TextWrapper textWrapper : ContentUtils.getTextWrappers(false)) {
      if (textWrapper.getResourceState() == ResourceState.MISSING) {
        System.out.println(ResourceState.MISSING + " text:  " + textWrapper.toString());
      } else if (textWrapper.getResourceState() == ResourceState.CORE) {
        tCore++;
      } else if (textWrapper.getResourceState() == ResourceState.MOD) {
        tMod++;
      }
    }
    
    final Vector<Class<?>> textEnums = new ClassFinder().findSubclasses(TextKey.class.getName());
    for (final Class<?> textEnum : textEnums) {
      final TextKey[] textKeys = (TextKey[]) textEnum.getEnumConstants();
      if (textKeys != null) {
        for (final TextKey key : textKeys) {
          final TextWrapper textWrapper = ResourceCache.get(key);
          if (textWrapper.getResourceState() == ResourceState.MISSING) {
            System.out.println(ResourceState.MISSING + " text:  " + key.getClass().getName() + "." + key.name());
          } else if (textWrapper.getResourceState() == ResourceState.CORE) {
            tCore++;
          } else if (textWrapper.getResourceState() == ResourceState.MOD) {
            tMod++;
          }
        }
      }
    }
    
    System.out.println();
    
    for (final File baseDir : PathFinder.getContentPaths()) {
      checkForOrphans(baseDir);
    }
    
    System.out.println();
    System.out.println(iCore + " core images.");
    System.out.println(iMod + " mod images.");
    System.out.println(tCore + " core texts.");
    System.out.println(tMod + " mod texts.");
    System.out.println("DONE.");
  }
  
  private static boolean containsLuckySurvivorDir(final File[] files) {
    for (final File file : files) {
      if (file.isDirectory() && file.getName().equals("LuckySurvivor")) {
        return true;
      }
    }
    return false;
  }
  
  private static void checkForOrphans(final File baseDir) {
    if (baseDir.isDirectory()) {
      for (final File file : baseDir.listFiles()) {
        checkPackageForOrphans(baseDir.getParent() + "/" + baseDir.getName(), file);
      }
    }
  }
  
  private static void checkPackageForOrphans(final String baseName, final File dir) {
    for (final File file : dir.listFiles()) {
      if (file.isFile()) {
        checkTextKey(baseName, dir.getName(), file.getName());
      } else {
        checkImageKey(baseName, dir.getName(), file.getName());
      }
    }
  }
  
  private static void checkTextKey(final String baseName, final String packageName, final String simpleClassnameAndName) {
    final String simpleClassname = simpleClassnameAndName.substring(0, simpleClassnameAndName.lastIndexOf('.'));
    final String name = simpleClassnameAndName.substring(simpleClassnameAndName.lastIndexOf('.') + 1);
    final String className = packageName + "." + simpleClassname;
    try {
      final TextKey key = (TextKey) Enum.valueOf((Class<? extends Enum>) Class.forName(className), name);
    } catch (final Exception e) {
      System.out.println("ORPHAN  text:  " + baseName + "/" + className + "." + name);
    }
  }
  
  private static void checkImageKey(final String baseName, final String className, final String name) {
    try {
      final ImageKey key = (ImageKey) Enum.valueOf((Class<? extends Enum>) Class.forName(className), name);
    } catch (final Exception e) {
      System.out.println("ORPHAN  image: " + baseName + "/" + className + "." + name);
    }
  }
}
