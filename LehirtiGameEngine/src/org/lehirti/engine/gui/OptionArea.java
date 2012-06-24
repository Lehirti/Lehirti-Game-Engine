package org.lehirti.engine.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;

import org.lehirti.engine.Main;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionArea extends JComponent implements Externalizable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(OptionArea.class);
  
  private final double screenX;
  private final double screenY;
  private final double sizeX;
  private final double sizeY;
  
  private final int cols;
  private final int rows;
  
  private final Map<Key, TextWrapper> options = new EnumMap<Key, TextWrapper>(Key.class);
  
  public OptionArea(final double screenX, final double screenY, final double sizeX, final double sizeY, final int cols,
      final int rows) {
    this.screenX = screenX;
    this.screenY = screenY;
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.cols = cols;
    this.rows = rows;
  }
  
  @Override
  public void paintComponent(final Graphics g) {
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, getWidth(), getHeight());
    g.setColor(Color.BLACK);
    final Dimension size = getSize();
    final Dimension sizeOfOneOptionField = new Dimension(size.width / this.cols, size.height / this.rows);
    final Font font = Main.TEXT_AREA.getScaledFont();
    g.setFont(font);
    final FontMetrics fontMetrics = g.getFontMetrics(font);
    final int fontHeight = fontMetrics.getHeight();
    final int yOffset = fontHeight + (sizeOfOneOptionField.height - fontHeight) / 4;
    for (final Map.Entry<Key, TextWrapper> option : this.options.entrySet()) {
      final Key key = option.getKey();
      final TextWrapper wrapper = ResourceCache.get(CommonText.KEY_OPTION);
      wrapper.addParameter(String.valueOf(key.mapping));
      final TextWrapper text = option.getValue();
      final String assembledOptionString = wrapper.getValue() + text.getValue();
      final int x = key.col * sizeOfOneOptionField.width;
      final int y = key.row * sizeOfOneOptionField.height + yOffset;
      
      g.drawRect(key.col * sizeOfOneOptionField.width, key.row * sizeOfOneOptionField.height,
          sizeOfOneOptionField.width, sizeOfOneOptionField.height);
      if (fontMetrics.stringWidth(assembledOptionString) < sizeOfOneOptionField.width) {
        g.drawString(assembledOptionString, x, y);
      } else {
        fitStringIntoOptionField(g, assembledOptionString, key.col * sizeOfOneOptionField.width, key.row
            * sizeOfOneOptionField.height, sizeOfOneOptionField, font.getSize());
        g.setFont(font);
      }
    }
  }
  
  private void fitStringIntoOptionField(final Graphics g, final String optionString, final int xOffset,
      final int yOffset, final Dimension optionField, final int fontSize) {
    final String[] words = optionString.split(" ");
    for (int i = fontSize; i > 0; i--) {
      final Font font = g.getFont();
      final Font newFont = new Font(font.getName(), font.getStyle(), i);
      final FontMetrics metrics = g.getFontMetrics(newFont);
      final int nrLines = optionField.height / metrics.getHeight();
      final List<String> lines = new ArrayList<String>(nrLines);
      int k = 0;
      for (int j = 0; j < nrLines; j++) {
        if (k >= words.length) {
          break;
        }
        final StringBuilder sb = new StringBuilder(words[k++]);
        while (k < words.length && metrics.stringWidth(sb.toString() + " " + words[k]) < optionField.width) {
          sb.append(" ");
          sb.append(words[k++]);
        }
        lines.add(sb.toString());
      }
      if (k >= words.length) {
        g.setFont(newFont);
        for (int j = 0; j < lines.size(); j++) {
          final int stringWidth = metrics.stringWidth(lines.get(j));
          final int xPad = (optionField.width - stringWidth) / 2;
          final int yPad = (optionField.height - lines.size() * metrics.getHeight()) / 4;
          g.drawString(lines.get(j), xOffset + xPad, yOffset + yPad + ((j + 1) * metrics.getHeight()));
        }
        return;
      }
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
  
  public List<TextWrapper> getAllOptions() {
    return new ArrayList<TextWrapper>(this.options.values());
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    this.options.clear();
    final int nrOfTexts = in.readInt();
    for (int i = 0; i < nrOfTexts; i++) {
      this.options.put((Key) in.readObject(), (TextWrapper) in.readObject());
    }
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeInt(this.options.size());
    for (final Entry<Key, TextWrapper> entry : this.options.entrySet()) {
      out.writeObject(entry.getKey());
      out.writeObject(entry.getValue());
    }
  }
  
  public void clearOptions() {
    this.options.clear();
    repaint();
  }
  
  // TODO: error on non-option keys
  public void setOption(final TextKey text, final Key key) {
    this.options.put(key, ResourceCache.get(text));
    repaint();
  }
}
