package lge.res.images;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import lge.res.ResourceState;
import lge.state.DateTime;
import lge.state.DateTime.DayPhase;
import lge.state.State;
import lge.util.PathFinder;

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
  
  private final ResourceState state;
  private final int nCore;
  private final int nMod;
  
  public ImageWrapper(final ImageKey key) {
    this.key = key;
    LOGGER.debug("Creating new ImageWrapper for {}", toString());
    this.proxies = new ArrayList<>(5);
    final File[] coreImageProxyFiles = PathFinder.getCoreImageProxyFiles(key);
    final File[] modImageProxyFiles = PathFinder.getModImageProxyFiles(key);
    
    // remove core files that are shadowed by mod files (e.g. core files that are marked as deleted)
    for (int i = 0; i < coreImageProxyFiles.length; i++) {
      final File file = coreImageProxyFiles[i];
      final String simpleCoreFileName = file.getName();
      for (final File modFile : modImageProxyFiles) {
        if (modFile.getName().equals(simpleCoreFileName)) {
          coreImageProxyFiles[i] = null;
          break;
        }
      }
    }
    
    this.nCore = parseAll(coreImageProxyFiles);
    this.nMod = parseAll(modImageProxyFiles);
    
    if (this.nCore > 0) {
      this.state = ResourceState.CORE;
    } else if (this.nMod > 0) {
      this.state = ResourceState.MOD;
    } else {
      this.state = ResourceState.MISSING;
    }
    
    pinRandomImage();
  }
  
  private int parseAll(final File[] imageProxies) {
    int ret = 0;
    for (final File imageProxyFile : imageProxies) {
      if (imageProxyFile == null) {
        continue;
      }
      LOGGER.debug("Trying to add image proxy {}", imageProxyFile.getAbsolutePath());
      final ImageProxy imageProxy = ImageProxy.getInstance(imageProxyFile);
      if (imageProxy != null) {
        LOGGER.debug("Adding image proxy {} to {}", imageProxyFile.getAbsolutePath(), toString());
        this.proxies.add(imageProxy);
        ret++;
      }
    }
    return ret;
  }
  
  public ResourceState getResourceState() {
    return this.state;
  }
  
  public int getNrOfCoreImages() {
    return this.nCore;
  }
  
  public int getNrOfModImages() {
    return this.nMod;
  }
  
  public int getCurrentImageNr() {
    return this.currentlyDisplayedImageNr;
  }
  
  public void pinRandomImage() {
    if (this.proxies.isEmpty()) {
      this.image = new ImageProxy(this.key);
    } else {
      this.currentlyDisplayedImageNr = State.DIE.nextInt(this.proxies.size());
      LOGGER.debug("Pin image {} alternative {}", toString(), Integer.valueOf(this.currentlyDisplayedImageNr));
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
  
  public boolean pinRandomTimeOfDayImage() {
    final DayPhase dayPhase = DateTime.getDayPhase();
    final List<ImageProxy> allImagesForTimeOfDay = getAllImagesForTimeOfDay(dayPhase);
    if (allImagesForTimeOfDay.contains(this.image)) {
      return false; // we can keep displaying the current image; nothing to do
    }
    // time-of-day has changed; we must display another image
    if (allImagesForTimeOfDay.isEmpty()) {
      this.image = new ImageProxy(this.key);
    } else {
      this.image = allImagesForTimeOfDay.get(State.DIE.nextInt(allImagesForTimeOfDay.size()));
      this.currentlyDisplayedImageNr = this.proxies.indexOf(this.image);
    }
    
    return true;
  }
  
  private List<ImageProxy> getAllImagesForTimeOfDay(final DayPhase dayPhase) {
    final List<ImageProxy> imagesForTimeOfDay = new LinkedList<>();
    for (final ImageProxy proxy : this.proxies) {
      if (dayPhase.name().equals(proxy.getAttribute())) {
        imagesForTimeOfDay.add(proxy);
      }
    }
    if (!imagesForTimeOfDay.isEmpty()) {
      return imagesForTimeOfDay;
    }
    
    // no exact match images found; using defaults
    for (final ImageProxy proxy : this.proxies) {
      if (proxy.getAttribute() == null) {
        imagesForTimeOfDay.add(proxy);
      }
    }
    return imagesForTimeOfDay;
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
  
  public AffineTransform getTransformation(final int width, final int height) {
    return this.image.getTransformation(width, height);
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
  
  public void removeAlternativeImage(final int alternativeImageNr) {
    final ImageProxy removed = this.proxies.remove(alternativeImageNr);
    this.currentlyDisplayedImageNr = this.proxies.size() - 1;
    this.image = new ImageProxy(this.key);
    removed.setDeleted();
    pinNextImage();
    LOGGER.info("Image {} removed from {}", removed.toString(), toString());
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
    return this.key.getClass().getName() + "." + this.key.name();
  }
  
  public String toButtonString() {
    return "<html>" + this.key.getClass().getSimpleName() + "<br/>" + this.key.name() + "</html>";
  }
  
  public void cache() {
    for (final ImageProxy proxy : this.proxies) {
      proxy.cache();
    }
  }
}
