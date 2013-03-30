package lge.xmlevents;

import java.awt.Dimension;

import javax.swing.JComboBox;

import lge.jaxb.KeyType;

public final class JKey extends JComboBox<KeyType> {
  private static final long serialVersionUID = 1L;
  
  public JKey() {
    super(KeyType.values());
    setEditable(false);
    setPreferredSize(new Dimension(80, 16));
  }
}
