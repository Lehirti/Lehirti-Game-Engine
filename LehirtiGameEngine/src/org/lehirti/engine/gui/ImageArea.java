package org.lehirti.engine.gui;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.VolatileImage;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImageKey.IO;
import org.lehirti.engine.res.images.ImageWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageArea extends JComponent implements Externalizable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(ImageArea.class);
  
  private VolatileImage backBuffer;
  
  ImageWrapper backgroundImage = null;
  
  final List<ImageWrapper> foregroundImages = new ArrayList<ImageWrapper>(15);
  
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
    
    final String interpolation = DisplayOptions.getDisplayOptionFor("INTERPOLATION", "BILINEAR");
    if ("NEAREST_NEIGHBOR".equals(interpolation)) {
      this.interpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
    } else if ("BILINEAR".equals(interpolation)) {
      this.interpolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
    } else {
      this.interpolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
    }
    
    final String renderQuality = DisplayOptions.getDisplayOptionFor("RENDERING", "DEFAULT");
    if ("SPEED".equals(renderQuality)) {
      this.renderQuality = RenderingHints.VALUE_RENDER_SPEED;
    } else if ("DEFAULT".equals(renderQuality)) {
      this.renderQuality = RenderingHints.VALUE_RENDER_DEFAULT;
    } else {
      this.renderQuality = RenderingHints.VALUE_RENDER_QUALITY;
    }
    
    final String antiAliasing = DisplayOptions.getDisplayOptionFor("ANTIALIASING", "OFF");
    if ("DEFAULT".equals(antiAliasing)) {
      this.antiAliasing = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
    } else if ("OFF".equals(antiAliasing)) {
      this.antiAliasing = RenderingHints.VALUE_ANTIALIAS_OFF;
    } else {
      this.antiAliasing = RenderingHints.VALUE_ANTIALIAS_ON;
    }
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    final long serialVersionUniqueID = in.readLong();
    if (serialVersionUniqueID == 1L) {
      readExternal1(in);
    } else {
      throw new RuntimeException("Unknown StateObject serialVersionUID: " + serialVersionUniqueID);
    }
  }
  
  private void readExternal1(final ObjectInput in) throws ClassNotFoundException, IOException {
    this.backgroundImage = null;
    this.foregroundImages.clear();
    
    final boolean hasBackgroundImage = in.readBoolean();
    if (hasBackgroundImage) {
      final ImageKey backgroundImageKey = IO.read(in);
      if (backgroundImageKey != null) {
        setBackgroundImage(backgroundImageKey);
      }
    }
    final int nrFG = in.readInt();
    for (int i = 0; i < nrFG; i++) {
      final ImageKey imgKey = IO.read(in);
      if (imgKey != null) {
        addImage(imgKey);
      }
    }
    repaint();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeLong(serialVersionUID);
    if (this.backgroundImage != null) {
      out.writeBoolean(true);
      ImageKey.IO.write(this.backgroundImage.getKey(), out);
    } else {
      out.writeBoolean(false);
    }
    out.writeInt(this.foregroundImages.size());
    for (final ImageWrapper img : this.foregroundImages) {
      ImageKey.IO.write(img.getKey(), out);
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
      g2d.setComposite(AlphaComposite.SrcAtop);
      
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, this.interpolation);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, this.renderQuality);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAliasing);
      
      drawImages(g2d);
      
      // copy to front buffer
      g.drawImage(this.backBuffer, 0, 0, this);
      
    } while (this.backBuffer.contentsLost());
  }
  
  void drawImages(final Graphics2D g2d) {
    if (this.backgroundImage != null) {
      final int[] coords = this.backgroundImage.calculateCoordinates(getWidth(), getHeight());
      g2d.drawImage(this.backgroundImage.getImage(), coords[0], coords[1], coords[2], coords[3], null);
    }
    
    for (final ImageWrapper image : this.foregroundImages) {
      final int[] coords = image.calculateCoordinates(getWidth(), getHeight());
      g2d.drawImage(image.getImage(), coords[0], coords[1], coords[2], coords[3], null);
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
    final ImageWrapper backgroundImage;
    if (imageKey != null) {
      backgroundImage = ResourceCache.get(imageKey);
      backgroundImage.pinRandomImage();
    } else {
      backgroundImage = null;
    }
    this.backgroundImage = backgroundImage;
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
    this.foregroundImages.remove(imageKey);
  }
  
  void setImage(final ImageWrapper images) {
    this.foregroundImages.clear();
    this.foregroundImages.add(images);
  }
  
  public List<ImageWrapper> getAllImages() {
    final List<ImageWrapper> allImages = new ArrayList<ImageWrapper>(16);
    if (this.backgroundImage != null) {
      allImages.add(this.backgroundImage);
    }
    allImages.addAll(this.foregroundImages);
    return allImages;
  }
}
