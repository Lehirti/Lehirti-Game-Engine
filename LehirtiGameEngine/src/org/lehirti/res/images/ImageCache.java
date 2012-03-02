package org.lehirti.res.images;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImageCache {
  final static File CORE_BASE_DIR = new File("core");
  final static File MOD_BASE_DIR = new File("mod");
  
  private static final Map<ImageKey, ImageWrapper> CACHE = new HashMap<ImageKey, ImageWrapper>();
  
  public static final synchronized ImageWrapper getImage(final ImageKey key) {
    ImageWrapper imageWrapper = CACHE.get(key);
    if (imageWrapper == null) {
      final File coreDir = getDir(CORE_BASE_DIR, key);
      final File modDir = getDir(MOD_BASE_DIR, key);
      imageWrapper = new ImageWrapper(key, coreDir, modDir);
      CACHE.put(key, imageWrapper);
    }
    return imageWrapper;
  }
  
  private static File getDir(final File baseDir, final ImageKey key) {
    final File moduleDir = new File(baseDir, key.getClass().getSimpleName());
    final File keyDir = new File(moduleDir, key.name());
    return keyDir;
  }
}
