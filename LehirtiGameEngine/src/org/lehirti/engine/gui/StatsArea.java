package org.lehirti.engine.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.JPanel;

import org.lehirti.engine.state.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatsArea extends JPanel implements Externalizable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(StatsArea.class);
  
  private final double screenX;
  private final double screenY;
  private final double sizeX;
  private final double sizeY;
  
  // Load/Save nothing to do
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    repaint();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
  }
  
  public StatsArea(final double screenX, final double screenY, final double sizeX, final double sizeY) {
    super(true);
    
    this.screenX = screenX;
    this.screenY = screenY;
    this.sizeX = sizeX;
    this.sizeY = sizeY;
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
  
  @Override
  public void paintComponent(final Graphics g) {
    clear(g);
    dateTime(g);
  }
  
  private void clear(final Graphics g) {
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, getWidth(), getHeight());
  }
  
  private static void dateTime(final Graphics g) {
    g.setColor(Color.BLACK);
    final String dateTimeString = DateTime.getDateFormatedForStatsArea();
    final Font font = Main.getCurrentTextArea().getScaledFont();
    g.setFont(font);
    g.drawString(dateTimeString, 0, font.getSize());
  }
}
