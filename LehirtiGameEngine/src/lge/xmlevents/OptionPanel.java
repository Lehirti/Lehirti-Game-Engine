package lge.xmlevents;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lge.jaxb.Event.Options.Option;
import lge.jaxb.KeyType;

public class OptionPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  private final JKey key = new JKey();
  private final JTextField text = new JTextField();
  private final JEvent event;
  
  public OptionPanel(final Option opt, final List<String> allCreatableClassEvents, final Set<String> allXMLEvents) {
    add(new JLabel("Option"));
    
    final KeyType keyText = opt.getKey();
    if (keyText != null) {
      this.key.setSelectedItem(keyText);
    }
    add(this.key);
    
    final String textText = opt.getText();
    if (textText != null) {
      this.text.setText(textText);
    }
    this.text.setPreferredSize(new Dimension(400, 16));
    add(this.text);
    
    final String[] allPossibleEvents = new String[allCreatableClassEvents.size() + allXMLEvents.size()];
    for (int i = 0; i < allCreatableClassEvents.size(); i++) {
      allPossibleEvents[i] = allCreatableClassEvents.get(i);
    }
    int i = 0;
    for (final String xmlEvent : allXMLEvents) {
      allPossibleEvents[allCreatableClassEvents.size() + i] = xmlEvent;
      i++;
    }
    Arrays.sort(allPossibleEvents);
    this.event = new JEvent(allPossibleEvents, true);
    final String eventText = opt.getEvent();
    if (eventText != null) {
      this.event.setSelectedItem(eventText);
    }
    add(this.event);
  }
  
  public Option getOption() {
    final Option opt = new Option();
    opt.setEvent((String) this.event.getSelectedItem());
    try {
      final KeyType kt = (KeyType) this.key.getSelectedItem();
      opt.setKey(kt);
    } catch (final Exception ignore) {
      
    }
    opt.setText(this.text.getText());
    return opt;
  }
}
