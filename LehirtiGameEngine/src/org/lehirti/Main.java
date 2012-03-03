package org.lehirti;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.lehirti.events.Event;
import org.lehirti.gui.ImageArea;
import org.lehirti.gui.Key;
import org.lehirti.gui.TextArea;
import org.lehirti.modules.ModuleInitializer;
import org.lehirti.util.ClassFinder;

public class Main {
  private static final String GAME_NAME = "Game";
  
  public static TextArea TEXT_AREA;
  public static ImageArea IMAGE_AREA;
  
  public static Event nextEvent = null;
  
  public static final Object LAST_KEY_TYPED_LOCK = new Object();
  public static Key LAST_KEY_TYPED;
  
  private static void createAndShowGUI() {
    
    final JFrame frame = new JFrame(GAME_NAME);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    TEXT_AREA = new TextArea();
    TEXT_AREA.addKeyListener(new KeyListener() {
      @Override
      public void keyPressed(final KeyEvent e) {
      }
      
      @Override
      public void keyReleased(final KeyEvent e) {
      }
      
      @Override
      public void keyTyped(final KeyEvent e) {
        final Key key = Key.getByChar(e.getKeyChar());
        if (key != null) {
          if (key == Key.CTRL_S) {
            final JFileChooser fc = new JFileChooser();
            final int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
              final File file = fc.getSelectedFile();
              IMAGE_AREA.getImageWrapper().addAlternativeImage(file);
            }
          } else {
            synchronized (LAST_KEY_TYPED_LOCK) {
              LAST_KEY_TYPED = key;
              LAST_KEY_TYPED_LOCK.notifyAll();
            }
          }
        }
        e.consume();
      }
    });
    
    IMAGE_AREA = new ImageArea();
    
    frame.getContentPane().add(IMAGE_AREA, BorderLayout.CENTER);
    frame.getContentPane().add(TEXT_AREA, BorderLayout.EAST);
    
    frame.pack();
    frame.setVisible(true);
  }
  
  protected static void engineMain(final String[] args) throws InterruptedException, InvocationTargetException {
    /*
     * load all modules
     */
    new ClassFinder().findSubclasses(ModuleInitializer.class.getName());
    
    javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
    
    while (true) {
      nextEvent.execute();
    }
  }
}