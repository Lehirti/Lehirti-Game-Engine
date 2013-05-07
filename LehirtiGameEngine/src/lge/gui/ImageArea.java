package lge.gui;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JComponent;

import lge.res.ResourceCache;
import lge.res.images.ImageKey;
import lge.res.images.ImageWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageArea extends JComponent {
  public static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(ImageArea.class);
  
  private VolatileImage backBuffer;
  
  final AtomicReference<ImageWrapper> backgroundImage = new AtomicReference<>();
  
  final ConcurrentLinkedQueue<ImageWrapper> foregroundImages = new ConcurrentLinkedQueue<>();
  
  private final double screenX;
  private final double screenY;
  private final double sizeX;
  private final double sizeY;
  
  private final Object interpolation;
  private final Object renderQuality;
  private final Object antiAliasing;
  
  public ImageArea(final double screenX, final double screenY, final double sizeX, final double sizeY) {
    this.screenX = screenX;
    this.screenY = screenY;
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    
    final String interpolationString = DisplayOptions.getDisplayOptionFor("INTERPOLATION", "BILINEAR");
    if ("NEAREST_NEIGHBOR".equals(interpolationString)) {
      this.interpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
    } else if ("BILINEAR".equals(interpolationString)) {
      this.interpolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
    } else {
      this.interpolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
    }
    
    final String renderQualityString = DisplayOptions.getDisplayOptionFor("RENDERING", "DEFAULT");
    if ("SPEED".equals(renderQualityString)) {
      this.renderQuality = RenderingHints.VALUE_RENDER_SPEED;
    } else if ("DEFAULT".equals(renderQualityString)) {
      this.renderQuality = RenderingHints.VALUE_RENDER_DEFAULT;
    } else {
      this.renderQuality = RenderingHints.VALUE_RENDER_QUALITY;
    }
    
    final String antiAliasingString = DisplayOptions.getDisplayOptionFor("ANTIALIASING", "OFF");
    if ("DEFAULT".equals(antiAliasingString)) {
      this.antiAliasing = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
    } else if ("OFF".equals(antiAliasingString)) {
      this.antiAliasing = RenderingHints.VALUE_ANTIALIAS_OFF;
    } else {
      this.antiAliasing = RenderingHints.VALUE_ANTIALIAS_ON;
    }
  }
  
  public void load(final ObjectInput in) throws IOException, ClassNotFoundException {
    final long serialVersionUniqueID = in.readLong();
    if (serialVersionUniqueID == 1L) {
      load1(in);
    } else {
      throw new RuntimeException("Unknown StateObject serialVersionUID: " + serialVersionUniqueID);
    }
  }
  
  private void load1(final ObjectInput in) throws ClassNotFoundException, IOException {
    this.backgroundImage.set(null);
    this.foregroundImages.clear();
    
    final boolean hasBackgroundImage = in.readBoolean();
    if (hasBackgroundImage) {
      this.backgroundImage.set((ImageWrapper) in.readObject());
    }
    final int nrFG = in.readInt();
    for (int i = 0; i < nrFG; i++) {
      this.foregroundImages.add((ImageWrapper) in.readObject());
    }
    repaint();
  }
  
  public void save(final ObjectOutput out) throws IOException {
    out.writeLong(serialVersionUID);
    if (this.backgroundImage.get() != null) {
      out.writeBoolean(true);
      out.writeObject(this.backgroundImage.get());
    } else {
      out.writeBoolean(false);
    }
    out.writeInt(this.foregroundImages.size());
    for (final ImageWrapper img : this.foregroundImages) {
      out.writeObject(img);
    }
  }
  
  @Override
  public void paintComponent(final Graphics g) {
    do {
      if (this.backBuffer != null) {
        this.backBuffer.flush();
        this.backBuffer = null;
      }
      this.backBuffer = createVolatileImage(getWidth(), getHeight());
      
      // rendering to the back buffer:
      final Graphics2D g2d = (Graphics2D) this.backBuffer.getGraphics();
      g2d.setComposite(AlphaComposite.SrcOver);
      
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, this.interpolation);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, this.renderQuality);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAliasing);
      
      drawImages(g2d);
      
      g2d.dispose();
      
      // copy to front buffer
      g.drawImage(this.backBuffer, 0, 0, this);
      
    } while (this.backBuffer.contentsLost());
  }
  
  void drawImages(final Graphics2D g2d) {
    final ImageWrapper bgImage = this.backgroundImage.get();
    if (bgImage != null) {
      bgImage.draw(g2d, getWidth(), getHeight());
    }
    
    for (final ImageWrapper image : this.foregroundImages) {
      image.draw(g2d, getWidth(), getHeight());
    }
  }
  
  @Override
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }
  
  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }
  
  @Override
  public Dimension getPreferredSize() {
    final Dimension size = getParent().getSize();
    double width = size.width;
    double height = size.height;
    if (width < 640) {
      width = 640;
    }
    if (height < 640) {
      height = 640;
    }
    
    final double baseSizeX = width / this.screenX;
    final double baseSizeY = height / this.screenY;
    final double baseSize = baseSizeX < baseSizeY ? baseSizeX : baseSizeY;
    
    width = baseSize * this.sizeX;
    height = baseSize * this.sizeY;
    
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("preferredSize: " + width + " " + height);
    }
    return new Dimension((int) width, (int) height);
  }
  
  /**
   * @param backgroundImage
   *          replace current background image with this one (null to remove background image)
   */
  public void setBackgroundImage(final ImageKey imageKey) {
    LOGGER.debug("Setting background image to {}", imageKey);
    final ImageWrapper bgImage;
    if (imageKey != null) {
      bgImage = ResourceCache.get(imageKey);
      bgImage.pinRandomTimeOfDayImage();
    } else {
      bgImage = null;
    }
    this.backgroundImage.set(bgImage);
  }
  
  /**
   * @return true, if image has changed due to a change in time-of-day
   */
  public boolean updateBackgroundImageForTimeOfDay() {
    LOGGER.debug("Updating background imag for time-of-day");
    final ImageWrapper imageWrapper = this.backgroundImage.get();
    if (imageWrapper != null) {
      return imageWrapper.pinRandomTimeOfDayImage();
    } else {
      return false;
    }
  }
  
  public void setImage(final ImageKey imageKey) {
    this.foregroundImages.clear();
    addImage(imageKey);
  }
  
  public void addImage(final ImageKey imageKey) {
    if (imageKey == null) {
      return;
    }
    final ImageWrapper image = ResourceCache.get(imageKey);
    image.pinRandomImage();
    this.foregroundImages.add(image);
  }
  
  public void removeImage(final ImageKey imageKey) {
    final Iterator<ImageWrapper> it = this.foregroundImages.iterator();
    while (it.hasNext()) {
      final ImageWrapper fgImage = it.next();
      if (fgImage.getKey() == imageKey) {
        it.remove();
      }
    }
  }
  
  public void setImage(final ImageWrapper image) {
    this.foregroundImages.clear();
    addImage(image);
  }
  
  public void addImage(final ImageWrapper image) {
    if (image == null) {
      return;
    }
    this.foregroundImages.add(image);
  }
  
  public List<ImageWrapper> getAllImages() {
    final List<ImageWrapper> allImages = new ArrayList<>(16);
    final ImageWrapper bgImage = this.backgroundImage.get();
    if (bgImage != null) {
      allImages.add(bgImage);
    }
    allImages.addAll(this.foregroundImages);
    return allImages;
  }
}
