package org.lehirti.gui;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextArea;

import org.lehirti.res.ResourceCache;
import org.lehirti.res.text.TextKey;
import org.lehirti.res.text.TextWrapper;

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
  
  public List<TextWrapper> getAllTexts() {
    // TODO
    final List<TextWrapper> allTexts = new LinkedList<TextWrapper>();
    allTexts.add(ResourceCache.get(new TextKey() {
      @Override
      public String name() {
        return "TODO Test 1";
      }
    }));
    allTexts.add(ResourceCache.get(new TextKey() {
      @Override
      public String name() {
        return "TODO Test 2";
      }
    }));
    return allTexts;
  }
}
