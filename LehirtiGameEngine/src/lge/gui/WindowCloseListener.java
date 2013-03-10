package lge.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class WindowCloseListener implements WindowListener {
  private final JFrame parent;
  private final String windowName;
  
  public WindowCloseListener(final JFrame parent, final String windowName) {
    this.parent = parent;
    this.windowName = windowName;
  }
  
  @Override
  public void windowOpened(final WindowEvent e) {
  }
  
  @Override
  public void windowClosing(final WindowEvent e) {
    final boolean isMax = (this.parent.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
    final Point loc = this.parent.getLocation();
    final Dimension size = this.parent.getSize();
    WindowLocation.store(this.windowName, isMax, loc.x, loc.y, size.width, size.height);
  }
  
  @Override
  public void windowClosed(final WindowEvent e) {
  }
  
  @Override
  public void windowIconified(final WindowEvent e) {
  }
  
  @Override
  public void windowDeiconified(final WindowEvent e) {
  }
  
  @Override
  public void windowActivated(final WindowEvent e) {
  }
  
  @Override
  public void windowDeactivated(final WindowEvent e) {
  }
  
}
