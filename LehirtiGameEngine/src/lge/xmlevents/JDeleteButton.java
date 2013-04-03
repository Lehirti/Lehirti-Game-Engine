package lge.xmlevents;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class JDeleteButton extends JButton {
  private static final long serialVersionUID = 1L;
  
  public JDeleteButton() {
    super("Delete");
    
    setForeground(Color.RED);
    
    addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        e.consume();
        final Container parent = getParent();
        final Container grandParent = parent.getParent();
        grandParent.remove(parent);
        grandParent.revalidate();
        grandParent.repaint();
      }
      
      @Override
      public void mousePressed(final MouseEvent e) {
      }
      
      @Override
      public void mouseReleased(final MouseEvent e) {
      }
      
      @Override
      public void mouseEntered(final MouseEvent e) {
      }
      
      @Override
      public void mouseExited(final MouseEvent e) {
      }
    });
  }
}
