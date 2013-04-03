package lge.xmlevents;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import lge.jaxb.Event;

public class JCreateButton extends JButton implements StatusUpdateable {
  private static final long serialVersionUID = 1L;
  
  private final JEvent event;
  
  public JCreateButton(final JEvent event) {
    super("Create");
    
    this.event = event;
    
    addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(final MouseEvent e) {
      }
      
      @Override
      public void mousePressed(final MouseEvent e) {
      }
      
      @Override
      public void mouseReleased(final MouseEvent e) {
        e.consume();
        Container ancestor = getParent();
        while (ancestor != null && !(ancestor instanceof EventEditor)) {
          ancestor = ancestor.getParent();
        }
        if (ancestor != null) {
          final EventEditor editor = (EventEditor) ancestor;
          
          editor.setCurrentEvent(new Event(), (String) JCreateButton.this.event.getSelectedItem());
          editor.setCurrentEventToScreen();
        }
      }
      
      @Override
      public void mouseEntered(final MouseEvent e) {
      }
      
      @Override
      public void mouseExited(final MouseEvent e) {
      }
    });
  }
  
  @Override
  public void updateStatus(final boolean eventContainsErrors) {
    if (eventContainsErrors) {
      setEnabled(false);
      setToolTipText("Cannot create a new event, while the current one contains errors.");
    } else if (this.event.canBeCreated()) {
      setEnabled(true);
      setToolTipText("Create new event: " + this.event.getSelectedItem());
    } else {
      setEnabled(false);
      setToolTipText("Cannot create event: " + this.event.getSelectedItem());
    }
  }
}
