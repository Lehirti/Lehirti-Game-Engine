package org.lehirti.gui;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import org.lehirti.modules.intro.IntroImage;
import org.lehirti.res.images.ImageCache;
import org.lehirti.res.images.ImageWrapper;

public class ImageArea extends JComponent {
  private static final int WIDTH = 1000;
  private static final int HEIGHT = 800;
  
  private int scaledWidth;
  private int scaledHeight;
  
  private ImageWrapper image;
  
  public ImageArea() {
    // try {
    this.image = ImageCache.getImage(IntroImage.INTRO_02);
    // final int imgWidth = this.image.getWidth();
    // final int imgHeight = this.image.getHeight();
    // if ((double) WIDTH / (double) HEIGHT > (double) imgWidth / (double)
    // imgHeight) {
    // this.scaledWidth = (int) (HEIGHT * ((double) imgWidth / (double)
    // imgHeight));
    // this.scaledHeight = HEIGHT;
    // } else {
    // this.scaledWidth = WIDTH;
    // this.scaledHeight = (int) (WIDTH * ((double) imgHeight) / imgWidth);
    // }
    // } catch (final IOException ex) {
    // // handle exception...
    // }
  }
  
  @Override
  public void paintComponent(final Graphics g) {
    final Graphics2D g2d = (Graphics2D) g;
    g2d.setComposite(AlphaComposite.Src);
    
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.drawImage(this.image.getRandomImage(), 0, 0, getWidth(), getHeight(), null);
  }
  
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(WIDTH, 800);
  }
  
  public void setImageWrapper(final ImageWrapper image) {
    this.image = image;
  }
  
  public ImageWrapper getImageWrapper() {
    return this.image;
  }
}
