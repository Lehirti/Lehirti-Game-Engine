package org.lehirti.gui;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.lehirti.res.ResourceCache;
import org.lehirti.res.images.ImageKey;
import org.lehirti.res.images.ImageWrapper;

public class ImageArea extends JComponent {
  private static final long serialVersionUID = 1L;
  
  private static final int WIDTH = 1000;
  private static final int HEIGHT = 800;
  
  private ImageWrapper backgroundImage = null;
  
  private final List<ImageWrapper> foregroundImages = new ArrayList<ImageWrapper>(15);
  
  @Override
  public void paintComponent(final Graphics g) {
    final Graphics2D g2d = (Graphics2D) g;
    g2d.setComposite(AlphaComposite.SrcAtop);
    
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    if (this.backgroundImage != null) {
      final int[] coords = this.backgroundImage.calculateCoordinates(getWidth(), getHeight());
      g.drawImage(this.backgroundImage.getImage(), coords[0], coords[1], coords[2], coords[3], null);
    }
    
    for (final ImageWrapper image : this.foregroundImages) {
      final int[] coords = image.calculateCoordinates(getWidth(), getHeight());
      g.drawImage(image.getImage(), coords[0], coords[1], coords[2], coords[3], null);
    }
  }
  
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(WIDTH, HEIGHT);
  }
  
  /**
   * @param backgroundImage
   *          replace current background image with this one (null to remove background image)
   */
  public void setBackgroundImage(final ImageKey imageKey) {
    final ImageWrapper backgroundImage = ResourceCache.get(imageKey);
    backgroundImage.pinRandomImage();
    this.backgroundImage = backgroundImage;
  }
  
  public void setImage(final ImageKey imageKey) {
    this.foregroundImages.clear();
    addImage(imageKey);
  }
  
  public void addImage(final ImageKey imageKey) {
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
  
  void setImages(final List<ImageWrapper> allImages) {
    this.foregroundImages.clear();
    this.foregroundImages.addAll(allImages);
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
