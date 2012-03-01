package org.lehirti.res.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
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
  
  public ImageWrapper(final ImageKey key, final File coreDir, final File modDir) {
    this.key = key;
    this.coreDir = coreDir;
    this.modDir = modDir;
    this.proxies.addAll(parseAll(this.coreDir));
    this.proxies.addAll(parseAll(this.modDir));
    
    // add "image missing" dummy, if necessary
    if (this.proxies.isEmpty()) {
      this.proxies.add(new ImageProxy(key));
    }
  }
  
  private Collection<? extends ImageProxy> parseAll(final File dir) {
    if (!dir.isDirectory()) {
      return Collections.emptyList();
    }
    final Collection<ImageProxy> images = new LinkedList<ImageProxy>();
    final File[] imageProxies = dir.listFiles(new FileFilter() {
      @Override
      public boolean accept(final File pathname) {
        return pathname.getName().endsWith(ImageProxy.FILENAME_SUFFIX);
      }
    });
    for (final File imageProxyFile : imageProxies) {
      final ImageProxy imageProxy = ImageProxy.getInstance(imageProxyFile);
      if (imageProxy != null) {
        images.add(imageProxy);
      }
    }
    return images;
  }
  
  public BufferedImage getRandomImage() {
    return this.proxies.get(GameState.DIE.nextInt(this.proxies.size())).getImage();
  }
}
