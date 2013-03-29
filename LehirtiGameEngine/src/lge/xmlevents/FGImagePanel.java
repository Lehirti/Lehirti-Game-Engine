package lge.xmlevents;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FGImagePanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  private final JTextField fgImage = new JTextField();
  
  public FGImagePanel(final String fgImage) {
    add(new JLabel("Foreground image"));
    if (fgImage != null) {
      this.fgImage.setText(fgImage);
    }
    this.fgImage.setPreferredSize(new Dimension(1000, 16));
    add(this.fgImage);
  }
  
  public String getFGImage() {
    return this.fgImage.getText();
  }
}
