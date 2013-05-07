package lge.res.images;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ImageProxyPath extends ImageProxy {
  private double x1;
  private double y1;
  private double x2;
  private double y2;
  private double centerX;
  private double centerY;
  private boolean isCurrent;
  
  public ImageProxyPath(final double x1, final double y1, final double x2, final double y2, final double centerX,
      final double centerY, final boolean isCurrent) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.centerX = centerX;
    this.centerY = centerY;
    this.isCurrent = isCurrent;
  }
  
  /**
   * for load/save
   */
  public ImageProxyPath() {
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeDouble(this.x1);
    out.writeDouble(this.y1);
    out.writeDouble(this.x2);
    out.writeDouble(this.y2);
    out.writeDouble(this.centerX);
    out.writeDouble(this.centerY);
    out.writeBoolean(this.isCurrent);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.x1 = in.readDouble();
    this.y1 = in.readDouble();
    this.x2 = in.readDouble();
    this.y2 = in.readDouble();
    this.centerX = in.readDouble();
    this.centerY = in.readDouble();
    this.isCurrent = in.readBoolean();
  }
  
  @Override
  protected void draw(final Graphics2D g2d, final int width, final int height) {
    Color color;
    if (this.isCurrent) {
      color = new Color(0.8f, 0f, 0f);
    } else {
      color = new Color(0.5f, 0.5f, 0.5f, 0.5f);
    }
    final Color oldColor = g2d.getColor();
    final Stroke oldStroke = g2d.getStroke();
    g2d.setColor(color);
    g2d.setStroke(new BasicStroke(width / 120f));
    g2d.drawLine(determinePosition(this.x1, this.centerX, width), determinePosition(this.y1, this.centerY, height),
        determinePosition(this.x2, this.centerX, width), determinePosition(this.y2, this.centerY, height));
    g2d.setColor(oldColor);
    g2d.setStroke(oldStroke);
  }
  
  private static int determinePosition(final double point, final double center, final int scale) {
    return (int) ((point - (center - 0.5)) * scale);
  }
}
