package org.lehirti.res;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.lehirti.res.images.ImageKey;
import org.lehirti.res.images.ImageWrapper;
import org.lehirti.res.text.TextKey;
import org.lehirti.res.text.TextWrapper;

public abstract class ResourceCache<KEY extends ResourceKey, VALUE> {
  public final static File CORE_BASE_DIR = new File("core");
  public final static File MOD_BASE_DIR = new File("mod");
  
  public static final String RES = "res";
  public static final File CORE_RES_DIR = new File(ResourceCache.CORE_BASE_DIR, RES);
  public static final File MOD_RES_DIR = new File(ResourceCache.MOD_BASE_DIR, RES);
  
  private static final ResourceCache<ImageKey, ImageWrapper> IMAGE_CACHE = new ResourceCache<ImageKey, ImageWrapper>() {
    @Override
    protected ImageWrapper getInstance(final ImageKey key, final File coreDir, final File modDir) {
      return new ImageWrapper(key, coreDir, modDir);
    }
  };
  
  private static final ResourceCache<TextKey, TextWrapper> TEXT_CACHE = new ResourceCache<TextKey, TextWrapper>() {
    @Override
    protected TextWrapper getInstance(final TextKey key, final File coreDir, final File modDir) {
      return new TextWrapper(key, coreDir, modDir);
    }
  };
  
  private final Map<KEY, VALUE> cache = new HashMap<KEY, VALUE>();
  
  private VALUE _get(final KEY key) {
    VALUE value = this.cache.get(key);
    if (value == null) {
      final File coreDir = getDir(CORE_BASE_DIR, key);
      final File modDir = getDir(MOD_BASE_DIR, key);
      value = getInstance(key, coreDir, modDir);
      this.cache.put(key, value);
    }
    return value;
  }
  
  private File getDir(final File baseDir, final KEY key) {
    final File moduleDir = new File(baseDir, key.getClass().getName());
    final File keyDir = new File(moduleDir, key.name());
    return keyDir;
  }
  
  protected abstract VALUE getInstance(KEY key, File coreDir, File modDir);
  
  public static final synchronized ImageWrapper get(final ImageKey key) {
    return IMAGE_CACHE._get(key);
  }
  
  public static final synchronized TextWrapper get(final TextKey key) {
    return TEXT_CACHE._get(key);
  }
}
