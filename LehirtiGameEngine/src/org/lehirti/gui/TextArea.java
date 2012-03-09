package org.lehirti.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import org.lehirti.res.text.TextWrapper;

public class TextArea extends JTextArea {
  private final List<TextWrapper> allTexts = new ArrayList<TextWrapper>(25);
  
  public TextArea() {
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
  public Dimension getPreferredSize() {
    return new Dimension(280, 800);
  }
  
  public void setText(final TextWrapper text) {
    this.allTexts.clear();
    addText(text);
  }
  
  public void addText(final TextWrapper text) {
    this.allTexts.add(text);
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
}
