package org.lehirti.gui;

import java.awt.Dimension;

import javax.swing.JTextArea;

import org.lehirti.res.ResourceCache;
import org.lehirti.res.text.TextKey;

public class TextArea extends JTextArea {
  public TextArea() {
    getCaret().setVisible(false);
    setLineWrap(true);
  }
  
  @Override
  public void repaint() {
    if (getCaret() != null) {
      getCaret().setVisible(false);
    }
    super.repaint();
  }
  
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(280, 800);
  }
  
  public void setText(final TextKey key) {
    setText(ResourceCache.get(key).getValue());
  }
  
  public void addText(final TextKey key) {
    setText(getText() + ResourceCache.get(key).getValue());
  }
}
