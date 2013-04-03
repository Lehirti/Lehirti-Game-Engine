package lge.xmlevents;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import lge.jaxb.Event.Extensions.Extension;
import lge.jaxb.KeyType;

public class ExtensionPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  private final JEvent event;
  private final JKey key = new JKey();
  private final JImageOrTextRef text;
  private final JDeleteButton deleteButton = new JDeleteButton();
  
  public ExtensionPanel(final Extension ext, final List<String> allClassEvents, final String[] allExternalTextRefs) {
    add(new JLabel("Extension"));
    final String[] possibleEvents = allClassEvents.toArray(new String[allClassEvents.size()]);
    Arrays.sort(possibleEvents);
    this.event = new JEvent(possibleEvents, Collections.<String> emptyList(), false);
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
    
    this.text = new JImageOrTextRef(allExternalTextRefs, false);
    final String textText = ext.getText();
    if (textText != null) {
      this.text.setSelectedItem(textText);
    }
    this.text.setPreferredSize(new Dimension(400, 16));
    add(this.text);
    
    add(this.deleteButton);
  }
  
  public Extension getExtension() {
    final Extension ext = new Extension();
    ext.setEvent((String) this.event.getSelectedItem());
    try {
      final KeyType kt = (KeyType) this.key.getSelectedItem();
      ext.setKey(kt);
    } catch (final Exception ignore) {
      
    }
    ext.setText((String) this.text.getSelectedItem());
    return ext;
  }
}
