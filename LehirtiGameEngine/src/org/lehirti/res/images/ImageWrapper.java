package org.lehirti.res.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.lehirit.state.GameState;

/**
 * Collection of all image alternatives representing one ImageKey
 */
public final class ImageWrapper {
  private final ImageKey key;
  private final File coreDir;
  private final File modDir;
  private final List<ImageProxy> proxies = new ArrayList<ImageProxy>(5);
  private final BufferedImage nullImage;
  
  public ImageWrapper(final ImageKey key, final File coreDir, final File modDir) {
    this.key = key;
    this.coreDir = coreDir;
    this.modDir = modDir;
    parseAll(this.coreDir);
    parseAll(this.modDir);
    
    if (this.proxies.isEmpty()) {
      this.nullImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    } else {
      this.nullImage = null;
    }
  }
  
  private void parseAll(final File dir) {
    if (!dir.isDirectory()) {
      return;
    }
    final File[] imageProxies = dir.listFiles(new FileFilter() {
      @Override
      public boolean accept(final File pathname) {
        return pathname.getName().endsWith(ImageProxy.FILENAME_SUFFIX);
      }
    });
    for (final File imageProxyFile : imageProxies) {
      final ImageProxy imageProxy = ImageProxy.getInstance(imageProxyFile);
      if (imageProxy != null) {
        this.proxies.add(imageProxy);
      }
    }
  }
  
  public BufferedImage getRandomImage() {
    if (this.proxies.isEmpty()) {
      return this.nullImage;
    }
    return this.proxies.get(GameState.DIE.nextInt(this.proxies.size())).getImage();
  }
  
  /**
   * @param alternativeImageFile
   * @return image added
   */
  public boolean addAlternativeImage(final File alternativeImageFile) {
    final ImageProxy imageProxy = ImageProxy.createNew(this.modDir, alternativeImageFile);
    if (imageProxy != null) {
      this.proxies.add(imageProxy);
      return true;
    }
    return false;
  }
  
}
