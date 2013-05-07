package lge.gui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import lge.progressgraph.PG;
import lge.progressgraph.ProgressGraph;
import lge.res.images.ImageWrapper;

public class ProgressImageArea extends ImageArea {
  private static final long serialVersionUID = 1L;
  
  public ProgressImageArea(final double screenX, final double screenY, final double sizeX, final double sizeY) {
    super(screenX, screenY, sizeX, sizeY);
  }
  
  @Override
  void drawImages(final Graphics2D g2d) {
    final ImageWrapper bgImage = this.backgroundImage.get();
    if (bgImage != null) {
      bgImage.draw(g2d, getWidth(), getHeight());
    }
    
    ProgressGraph pg = null;
    
    // offset in x direction for progress graph
    int xOffset = 0;
    
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
      final AffineTransform transformation = image.getTransformation(getWidth(), getHeight());
      final int xOffsetOfOneImage = computeMaxXOffset(transformation, bImg.getWidth(), bImg.getHeight());
      xOffset = xOffset > xOffsetOfOneImage ? xOffset : xOffsetOfOneImage;
      g2d.drawImage(bImg, transformation, null);
    }
    
    if (pg != null) {
      final Image dynamicGraphImage = pg.getDynamicGraphImage(getWidth() - xOffset, getHeight());
      g2d.drawImage(dynamicGraphImage, xOffset, 0, getWidth() - xOffset, getHeight(), null);
    }
  }
  
  private static int computeMaxXOffset(final AffineTransform transformation, final int width, final int height) {
    final Point2D[] ptSrc = new Point2D[] { new Point(0, 0), new Point(0, height), new Point(width, 0),
        new Point(width, height) };
    final Point2D[] ptDst = new Point2D[4];
    transformation.transform(ptSrc, 0, ptDst, 0, 4);
    long maxXOffset = 0;
    for (final Point2D pt : ptDst) {
      if (Math.round(pt.getX()) > maxXOffset) {
        maxXOffset = Math.round(pt.getX());
      }
    }
    return (int) maxXOffset;
  }
}
