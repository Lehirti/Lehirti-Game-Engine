package lge.xmlevents;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lge.jaxb.Event.Extensions.Extension;
import lge.jaxb.KeyType;

public class ExtensionPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  private final JEvent event;
  private final JKey key = new JKey();
  private final JTextField text = new JTextField();
  
  public ExtensionPanel(final Extension ext, final List<String> allClassEvents) {
    add(new JLabel("Extension"));
    final String[] possibleEvents = allClassEvents.toArray(new String[allClassEvents.size()]);
    Arrays.sort(possibleEvents);
    this.event = new JEvent(possibleEvents, false);
    final String eventText = ext.getEvent();
    if (eventText != null) {
      this.event.setSelectedItem(eventText);
    }
    add(this.event);
    
    final KeyType keyText = ext.getKey();
    if (keyText != null) {
      this.key.setSelectedItem(keyText);
    }
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
    ext.setEvent((String) this.event.getSelectedItem());
    try {
      final KeyType kt = (KeyType) this.key.getSelectedItem();
      ext.setKey(kt);
    } catch (final Exception ignore) {
      
    }
    ext.setText(this.text.getText());
    return ext;
  }
  
}
