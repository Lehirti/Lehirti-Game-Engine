package lge.xmlevents;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class FGImagePanel extends JPanel {
  private static final long serialVersionUID = 1L;
  
  private final JImageOrTextRef fgImage;
  
  public FGImagePanel(final String fgImage, final String[] possibleRefs) {
    add(new JLabel("Foreground image"));
    this.fgImage = new JImageOrTextRef(possibleRefs);
    if (fgImage != null) {
      this.fgImage.setSelectedItem(fgImage);
    }
    this.fgImage.setPreferredSize(new Dimension(1000, 16));
    add(this.fgImage);
  }
  
  public String getFGImage() {
    return (String) this.fgImage.getSelectedItem();
  }
}
