package lge.xmlevents;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lge.jaxb.Event.Extensions.Extension;
import lge.jaxb.KeyType;

public class ExtensionPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  private final JTextField event = new JTextField();
  private final JTextField key = new JTextField();
  private final JTextField text = new JTextField();
  
  public ExtensionPanel(final Extension ext) {
    add(new JLabel("Extension"));
    final String eventText = ext.getEvent();
    if (eventText != null) {
      this.event.setText(eventText);
    }
    this.event.setPreferredSize(new Dimension(400, 16));
    add(this.event);
    
    final KeyType keyText = ext.getKey();
    if (keyText != null) {
      this.key.setText(keyText.name());
    }
    this.key.setPreferredSize(new Dimension(32, 16));
    add(this.key);
    
    final String textText = ext.getText();
    if (textText != null) {
      this.text.setText(textText);
    }
    this.text.setPreferredSize(new Dimension(400, 16));
    add(this.text);
  }
  
  public Extension getExtension() {
    final Extension ext = new Extension();
    ext.setEvent(this.event.getText());
    try {
      final KeyType kt = KeyType.valueOf(this.key.getText());
      ext.setKey(kt);
    } catch (final Exception ignore) {
      
    }
    ext.setText(this.text.getText());
    return ext;
  }
  
}
