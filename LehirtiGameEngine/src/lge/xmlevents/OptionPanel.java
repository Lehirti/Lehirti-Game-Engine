package lge.xmlevents;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lge.jaxb.Event.Options.Option;
import lge.jaxb.KeyType;

public class OptionPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  private final JTextField key = new JTextField();
  private final JTextField text = new JTextField();
  private final JTextField event = new JTextField();
  
  public OptionPanel(final Option opt) {
    add(new JLabel("Option"));
    
    final KeyType keyText = opt.getKey();
    if (keyText != null) {
      this.key.setText(keyText.name());
    }
    this.key.setPreferredSize(new Dimension(32, 16));
    add(this.key);
    
    final String textText = opt.getText();
    if (textText != null) {
      this.text.setText(textText);
    }
    this.text.setPreferredSize(new Dimension(400, 16));
    add(this.text);
    
    final String eventText = opt.getEvent();
    if (eventText != null) {
      this.event.setText(eventText);
    }
    this.event.setPreferredSize(new Dimension(400, 16));
    add(this.event);
  }
  
  public Option getOption() {
    final Option opt = new Option();
    opt.setEvent(this.event.getText());
    try {
      final KeyType kt = KeyType.valueOf(this.key.getText());
      opt.setKey(kt);
    } catch (final Exception ignore) {
      
    }
    opt.setText(this.text.getText());
    return opt;
  }
}
