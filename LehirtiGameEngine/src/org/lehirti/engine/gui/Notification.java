package org.lehirti.engine.gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.Timer;

import org.lehirti.engine.res.text.TextWrapper;

public class Notification extends JDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  
  public Notification(final Window owner, final String text) {
    super(owner, ModalityType.APPLICATION_MODAL);
    
    final JLabel textLabel = new JLabel(text);
    
    setContentPane(textLabel);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
  
  public Notification(final Window owner, final TextWrapper textWrapper, final int displayTimeinMillis) {
    super(owner, ModalityType.APPLICATION_MODAL);
    
    final JLabel text = new JLabel(textWrapper.getValue());
    
    setContentPane(text);
    pack();
    setLocationRelativeTo(null);
    final Timer timer = new Timer(displayTimeinMillis, this);
    timer.setRepeats(false);
    timer.start();
    setVisible(true);
  }
  
  @Override
  public void actionPerformed(final ActionEvent e) {
    dispose();
  }
}
