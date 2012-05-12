package org.lehirti.engine.gui;

import java.awt.Dimension;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import org.lehirti.engine.res.text.TextWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextArea extends JTextArea implements Externalizable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(TextArea.class);
  
  private final List<TextWrapper> allTexts = new ArrayList<TextWrapper>(25);
  
  private final double screenX;
  private final double screenY;
  private final double sizeX;
  private final double sizeY;
  
  public TextArea(final double screenX, final double screenY, final double sizeX, final double sizeY) {
    this.screenX = screenX;
    this.screenY = screenY;
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    
    getCaret().setVisible(false);
    setLineWrap(true);
    setWrapStyleWord(true);
  }
  
  @Override
  public void repaint() {
    if (getCaret() != null) {
      getCaret().setVisible(false);
    }
    super.repaint();
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
  
  public void setText(final TextWrapper text) {
    this.allTexts.clear();
    addText(text);
  }
  
  public void addText(final TextWrapper text) {
    if (text != null) {
      this.allTexts.add(text);
    }
    refresh();
  }
  
  public void refresh() {
    final StringBuilder sb = new StringBuilder();
    for (final TextWrapper text : this.allTexts) {
      sb.append(text.getValue());
    }
    setText(sb.toString());
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
    refresh();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeInt(this.allTexts.size());
    for (final TextWrapper text : this.allTexts) {
      out.writeObject(text);
    }
  }
}
