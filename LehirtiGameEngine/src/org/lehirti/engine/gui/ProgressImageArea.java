package org.lehirti.engine.gui;

import java.awt.Graphics2D;
import java.awt.Image;

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
    
    for (final ImageWrapper image : this.foregroundImages) {
      if (image.getKey() instanceof PG) {
        pg = ((PG) image.getKey()).getProgressGraph();
      }
      final int[] coords = image.calculateCoordinates(getWidth(), getHeight());
      g2d.drawImage(image.getImage(), coords[0], coords[1], coords[2], coords[3], null);
    }
    
    if (pg != null) {
      final Image dynamicGraphImage = pg.getDynamicGraphImage(getWidth(), getHeight());
      g2d.drawImage(dynamicGraphImage, 0, 0, getWidth(), getHeight(), null);
    }
  }
}
