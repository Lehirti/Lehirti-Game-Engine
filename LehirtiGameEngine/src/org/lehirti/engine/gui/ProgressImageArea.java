package org.lehirti.engine.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.lehirti.engine.progressgraph.PG;
import org.lehirti.engine.progressgraph.ProgressGraph;
import org.lehirti.engine.res.images.ImageWrapper;

public class ProgressImageArea extends ImageArea {
  public ProgressImageArea(final double screenX, final double screenY, final double sizeX, final double sizeY) {
    super(screenX, screenY, sizeX, sizeY);
  }
  
  @Override
  void drawImages(final Graphics2D g2d) {
    if (this.backgroundImage != null) {
      final int[] coords = this.backgroundImage.calculateCoordinates(getWidth(), getHeight());
      g2d.drawImage(this.backgroundImage.getImage(), coords[0], coords[1], coords[2], coords[3], null);
    }
    
    ProgressGraph pg = null;
    int[] coords = null;
    
    for (final ImageWrapper image : this.foregroundImages) {
      BufferedImage bImg = image.getImage();
      if (image.getKey() instanceof PG) {
        pg = ((PG) image.getKey()).getProgressGraph();
        if (pg.getActiveNode().isRoot()) {
          final BufferedImage bImg2 = new BufferedImage(bImg.getWidth(), bImg.getHeight(), bImg.getType());
          for (int x = 0; x < bImg.getWidth(); x++) {
            for (int y = 0; y < bImg.getHeight(); y++) {
              int argb = bImg.getRGB(x, y);
              argb &= 0xFF000000;
              argb += argb >> 8;
              argb += argb >> 16;
              bImg2.setRGB(x, y, argb);
            }
          }
          bImg = bImg2;
        }
      }
      coords = image.calculateCoordinates(getWidth(), getHeight());
      g2d.drawImage(bImg, coords[0], coords[1], coords[2], coords[3], null);
    }
    
    if (pg != null) {
      final int xOffset = coords[0] + coords[2];
      final Image dynamicGraphImage = pg.getDynamicGraphImage(getWidth() - xOffset, getHeight());
      g2d.drawImage(dynamicGraphImage, xOffset, 0, getWidth() - xOffset, getHeight(), null);
    }
  }
}
