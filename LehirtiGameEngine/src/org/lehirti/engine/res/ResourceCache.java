package org.lehirti.engine.res;

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImageWrapper;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResourceCache<KEY extends ResourceKey, VALUE> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResourceCache.class);
  
  private static final Map<String, BufferedImage> RAW_IMAGE_CACHE = new LinkedHashMap<String, BufferedImage>() {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected boolean removeEldestEntry(final Map.Entry<String, BufferedImage> eldest) {
      return size() > 50; // TODO make cache size configurable
    }
  };
  
  private static final BlockingQueue<ImageKey> IMAGES_TO_PRELOAD = new LinkedBlockingQueue<>();
  
  private static final ResourceCache<ImageKey, ImageWrapper> IMAGE_CACHE = new ResourceCache<ImageKey, ImageWrapper>() {
    @Override
    protected ImageWrapper getInstance(final ImageKey key) {
      return new ImageWrapper(key);
    }
  };
  
  private final Map<KEY, VALUE> cache = new HashMap<>();
  
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
  
  @CheckForNull
  public static final synchronized TextWrapper get(final @Nullable TextKey key) {
    if (key == null) {
      return null;
    }
    return new TextWrapper(key, false);
  }
  
  public static void cacheRawImage(final File imageFile, final BufferedImage bufferedImage) {
    synchronized (RAW_IMAGE_CACHE) {
      RAW_IMAGE_CACHE.put(imageFile.getName(), bufferedImage);
    }
  }
  
  public static BufferedImage getRawImage(final File imageFile) {
    synchronized (RAW_IMAGE_CACHE) {
      BufferedImage bufferedImage = RAW_IMAGE_CACHE.get(imageFile.getName());
      if (bufferedImage != null) {
        return bufferedImage;
      } else {
        try {
          final BufferedImage tmp = ImageIO.read(imageFile);
          bufferedImage = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
              .getDefaultConfiguration().createCompatibleImage(tmp.getWidth(), tmp.getHeight(), tmp.getTransparency());
          final Graphics2D g2d = bufferedImage.createGraphics();
          g2d.drawImage(tmp, null, 0, 0);
          g2d.dispose();
          bufferedImage.setAccelerationPriority(0.4f);
          RAW_IMAGE_CACHE.put(imageFile.getName(), bufferedImage);
          return bufferedImage;
        } catch (final IOException e) {
          LOGGER.error("Failed to read image " + imageFile.getAbsolutePath(), e);
          return null;
        }
      }
    }
  }
  
  public static void cache(final ImageKey imageKey) {
    final ImageWrapper imageWrapper = get(imageKey);
    imageWrapper.cache();
  }
  
  public static BlockingQueue<ImageKey> getImagesToPreload() {
    return IMAGES_TO_PRELOAD;
  }
}
