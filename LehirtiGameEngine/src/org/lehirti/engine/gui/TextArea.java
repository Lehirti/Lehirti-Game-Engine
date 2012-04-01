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

public class TextArea extends JTextArea implements Externalizable {
  private static final long serialVersionUID = 1L;
  
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
