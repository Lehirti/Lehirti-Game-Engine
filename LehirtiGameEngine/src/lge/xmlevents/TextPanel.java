package lge.xmlevents;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TextPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  private final JImageOrTextRef text;
  
  public TextPanel(final String textText, final String[] possibleRefs) {
    add(new JLabel("Text"));
    this.text = new JImageOrTextRef(possibleRefs);
    if (textText != null) {
      this.text.setSelectedItem(textText);
    }
    this.text.setPreferredSize(new Dimension(1000, 16));
    add(this.text);
  }
  
  public String getText() {
    return (String) this.text.getSelectedItem();
  }
}
