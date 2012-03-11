package org.lehirti.res;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.res.images.ImageKey;
import org.lehirti.res.images.ImageWrapper;
import org.lehirti.res.text.TextKey;
import org.lehirti.res.text.TextWrapper;

public abstract class ResourceCache<KEY extends ResourceKey, VALUE> {
  private static final ResourceCache<ImageKey, ImageWrapper> IMAGE_CACHE = new ResourceCache<ImageKey, ImageWrapper>() {
    @Override
    protected ImageWrapper getInstance(final ImageKey key) {
      return new ImageWrapper(key);
    }
  };
  
  private final Map<KEY, VALUE> cache = new HashMap<KEY, VALUE>();
  
  private VALUE _get(final KEY key) {
    VALUE value = this.cache.get(key);
    if (value == null) {
      value = getInstance(key);
      this.cache.put(key, value);
    }
    return value;
  }
  
  protected abstract VALUE getInstance(KEY key);
  
  public static final synchronized ImageWrapper get(final ImageKey key) {
    return IMAGE_CACHE._get(key);
  }
  
  public static final synchronized TextWrapper get(final TextKey key) {
    return new TextWrapper(key);
  }
}
