package org.lehirti.res.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.lehirti.state.StateObject;
import org.lehirti.util.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collection of all image alternatives representing one ImageKey
 */
public final class ImageWrapper {
  private static final Logger LOGGER = LoggerFactory.getLogger(ImageWrapper.class);
  
  private final ImageKey key;
  private final List<ImageProxy> proxies;
  private ImageProxy image;
  private int currentlyDisplayedImageNr = -1;
  
  public ImageWrapper(final ImageKey key) {
    this.key = key;
    LOGGER.debug("Creating new ImageWrapper for {}", toString());
    this.proxies = new ArrayList<ImageProxy>(5);
    parseAll(PathFinder.getCoreImageProxyFiles(key));
    parseAll(PathFinder.getModImageProxyFiles(key));
    
    pinRandomImage();
  }
  
  private void parseAll(final File[] imageProxies) {
    for (final File imageProxyFile : imageProxies) {
      LOGGER.debug("Trying to add image proxy {}", imageProxyFile.getAbsolutePath());
      final ImageProxy imageProxy = ImageProxy.getInstance(imageProxyFile);
      if (imageProxy != null) {
        LOGGER.debug("Adding image proxy {} to {}", imageProxyFile.getAbsolutePath(), toString());
        this.proxies.add(imageProxy);
      }
    }
  }
  
  public int getCurrentImageNr() {
    return this.currentlyDisplayedImageNr;
  }
  
  public void pinRandomImage() {
    if (this.proxies.isEmpty()) {
      this.image = new ImageProxy();
    } else {
      this.currentlyDisplayedImageNr = StateObject.DIE.nextInt(this.proxies.size());
      LOGGER.debug("Pin image {} alternative {}", toString(), this.currentlyDisplayedImageNr);
      this.image = this.proxies.get(this.currentlyDisplayedImageNr);
    }
  }
  
  public void pinNextImage() {
    if (this.proxies.isEmpty()) {
      return;
    } else {
      this.currentlyDisplayedImageNr++;
      this.currentlyDisplayedImageNr %= this.proxies.size();
      this.image = this.proxies.get(this.currentlyDisplayedImageNr);
    }
  }
  
  public void setPlacement(final Properties placement) {
    this.image.setPlacement(placement);
    this.image.writeProxyFile();
  }
  
  public Properties getPlacement() {
    return this.image.getPlacement();
  }
  
  public BufferedImage getImage() {
    return this.image.getImage();
  }
  
  public int[] calculateCoordinates(final int width, final int height) {
    return this.image.calculateCoordinates(width, height);
  }
  
  /**
   * @param alternativeImageFile
   * @param contentDir
   * @return image added
   */
  public boolean addAlternativeImage(final File alternativeImageFile, final String contentDir) {
    final ImageProxy imageProxy = ImageProxy.createNew(PathFinder.getModImageProxyFile(this.key, contentDir),
        alternativeImageFile);
    if (imageProxy != null) {
      this.proxies.add(imageProxy);
      this.currentlyDisplayedImageNr = this.proxies.size() - 1;
      this.image = imageProxy;
      LOGGER.info("New image {} added to {}", alternativeImageFile.getAbsolutePath(), toString());
      return true;
    } else {
      LOGGER.error("Failed to create new image from " + alternativeImageFile.getAbsolutePath());
    }
    return false;
  }
  
  public ImageKey getKey() {
    return this.key;
  }
  
  @Override
  public int hashCode() {
    return this.key.hashCode();
  }
  
  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof ImageWrapper)) {
      return false;
    }
    final ImageWrapper other = (ImageWrapper) o;
    return this.key.equals(other.key);
  }
  
  @Override
  public String toString() {
    return this.key.getClass().getSimpleName() + "." + this.key.name();
  }
  
  public String toButtonString() {
    return "<html>" + this.key.getClass().getSimpleName() + "<br/>" + this.key.name() + "</html>";
  }
}
