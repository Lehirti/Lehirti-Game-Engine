package org.lehirti;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;

import org.lehirti.events.Event;
import org.lehirti.gui.ImageArea;
import org.lehirti.gui.ImageEditor;
import org.lehirti.gui.Key;
import org.lehirti.gui.TextArea;
import org.lehirti.gui.TextEditor;
import org.lehirti.res.images.ImageWrapper;
import org.lehirti.state.StaticInitializer;
import org.lehirti.util.ClassFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Main {
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  
  private static final String VERSION_FILE_LOCATION = "version";
  
  public static TextArea TEXT_AREA;
  public static ImageArea IMAGE_AREA;
  
  public static Event nextEvent = null;
  
  public static final Object LAST_KEY_TYPED_LOCK = new Object();
  public static Key LAST_KEY_TYPED;
  
  public static final Random DIE = new Random();
  
  private void createAndShowGUI() {
    
    final JFrame frame = new JFrame(getGameName());
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
          if (key == Key.CTRL_I) {
            editImages();
          } else if (key == Key.CTRL_T) {
            editTexts();
          } else {
            synchronized (LAST_KEY_TYPED_LOCK) {
              LAST_KEY_TYPED = key;
              LAST_KEY_TYPED_LOCK.notifyAll();
            }
          }
        }
        e.consume();
      }
      
      private void editImages() {
        final List<ImageWrapper> allImages = IMAGE_AREA.getAllImages();
        new ImageEditor(allImages, IMAGE_AREA);
      }
      
      private void editTexts() {
        new TextEditor(TEXT_AREA);
      }
    });
    
    IMAGE_AREA = new ImageArea();
    
    frame.getContentPane().add(IMAGE_AREA, BorderLayout.CENTER);
    frame.getContentPane().add(TEXT_AREA, BorderLayout.EAST);
    
    frame.pack();
    frame.setVisible(true);
  }
  
  protected void engineMain(final String[] args) throws InterruptedException, InvocationTargetException {
    logVersion();
    
    /*
     * load all modules
     */
    final Vector<Class<?>> modules = new ClassFinder().findSubclasses(StaticInitializer.class.getName());
    for (final Class<?> module : modules) {
      LOGGER.debug("Loaded module: {}", module.getName());
    }
    
    javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
    
    LOGGER.info("{} started.", getGameName());
    
    while (true) {
      nextEvent.execute();
    }
  }
  
  private void logVersion() {
    final InputStream versionInputStream = ClassLoader.getSystemResourceAsStream(VERSION_FILE_LOCATION);
    final Properties versionProps = new Properties();
    try {
      versionProps.load(versionInputStream);
    } catch (final Exception e) {
      LOGGER.info(getGameName() + " Development Version");
      return;
    }
    
    LOGGER.info(getGameName() + " " + versionProps.getProperty("number") + " " + versionProps.getProperty("flavor")
        + " Build " + versionProps.getProperty("build") + " " + versionProps.getProperty("date"));
  }
  
  abstract protected String getGameName();
}