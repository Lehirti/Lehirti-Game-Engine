package org.lehirti.engine.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionArea extends JComponent implements Externalizable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(OptionArea.class);
  
  private final List<TextWrapper> allTexts = new ArrayList<TextWrapper>(25);
  
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
    final Dimension size = getSize();
    final Dimension sizeOfOneOptionField = new Dimension(size.width / this.cols, size.height / this.rows);
    for (final Map.Entry<Key, TextWrapper> option : this.options.entrySet()) {
      final Key key = option.getKey();
      final TextWrapper wrapper = ResourceCache.get(CommonText.KEY_OPTION);
      wrapper.addParameter(String.valueOf(key.mapping));
      final TextWrapper text = option.getValue();
      final String assembledOptionString = wrapper.getValue() + text.getValue();
      final int x = key.col * sizeOfOneOptionField.width;
      final int y = (int) ((key.row + 0.8) * sizeOfOneOptionField.height);
      g.drawString(assembledOptionString, x, y);
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
    
    LOGGER.info("preferredSize: " + width + " " + height);
    return new Dimension((int) width, (int) height);
  }
  
  public List<TextWrapper> getAllTexts() {
    return this.allTexts;
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    this.allTexts.clear();
    final int nrOfTexts = in.readInt();
    for (int i = 0; i < nrOfTexts; i++) {
      this.allTexts.add((TextWrapper) in.readObject());
    }
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeInt(this.allTexts.size());
    for (final TextWrapper text : this.allTexts) {
      out.writeObject(text);
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
