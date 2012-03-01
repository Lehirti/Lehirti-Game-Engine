package org.lehirti.gui;

import java.awt.Dimension;

import javax.swing.JTextArea;

public class TextArea extends JTextArea {
  public TextArea() {
    getCaret().setVisible(false);
  }
  
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(280, 800);
  }
}
