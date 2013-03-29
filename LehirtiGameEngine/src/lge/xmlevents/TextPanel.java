package lge.xmlevents;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  private final JTextField text = new JTextField();
  
  public TextPanel(final String textText) {
    add(new JLabel("Text"));
    if (textText != null) {
      this.text.setText(textText);
    }
    this.text.setPreferredSize(new Dimension(1000, 16));
    add(this.text);
  }
  
  public String getText() {
    return this.text.getText();
  }
}
