package org.lehirti;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JFrame;

import org.lehirti.events.Event;
import org.lehirti.gui.ImageArea;
import org.lehirti.gui.ImageEditor;
import org.lehirti.gui.Key;
import org.lehirti.gui.TextArea;
import org.lehirti.gui.TextEditor;
import org.lehirti.res.images.ImageWrapper;
import org.lehirti.state.StateObject;
import org.lehirti.state.StaticInitializer;
import org.lehirti.util.ClassFinder;
import org.lehirti.util.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Main {
  static {
    final File logsDir = new File("logs");
    if (!logsDir.mkdirs()) {
      System.err.println("Could not create logs dir " + logsDir.getAbsolutePath());
    }
    System.setProperty("java.util.logging.config.file", "config/logging.properties");
  }
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  
  public static TextArea TEXT_AREA;
  public static ImageArea IMAGE_AREA;
  
  public static volatile Event currentEvent = null;
  
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
      public synchronized void keyTyped(final KeyEvent e) {
        // note the synchronized: making sure to only proccess one key event at a time
        try {
          final Key key = Key.getByChar(e.getKeyChar());
          
          if (key == null) {
            // key unrelated to the game pressed
            return;
          }
          
          // let current event handle key event first
          if (getCurrentEvent() != null) {
            if (getCurrentEvent().handleKeyEvent(key)) {
              // current event did use the key, so this key is "used up"
              return;
            }
          }
          
          // TODO let the game handle key event and only if it does not use it
          
          // let the game engine handle key event
          if (key == Key.CTRL_I) {
            editImages();
          } else if (key == Key.CTRL_T) {
            editTexts();
          } else if (key == Key.CTRL_S) {
            saveGame();
          } else if (key == Key.CTRL_L) {
            loadGame();
          } else {
            // key known to the game, but currently not assigned (e.g. one of the option keys)
          }
        } finally {
          e.consume();
        }
        
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
  
  protected void loadGame() {
    final File sav = new File("save"); // TODO
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    try {
      if (currentEvent == null || !currentEvent.isLoadSavePoint()) {
        LOGGER.warn("Another game cannot be loaded right now"); // better notification for user
        return;
      }
      
      fis = new FileInputStream(sav);
      ois = new ObjectInputStream(fis);
      StateObject.load(ois);
      IMAGE_AREA.readExternal(ois);
      TEXT_AREA.readExternal(ois);
      final Event oldEvent = currentEvent;
      currentEvent = (Event) ois.readObject();
      synchronized (oldEvent) {
        oldEvent.newEventHasBeenLoaded();
        oldEvent.notifyAll();
      }
      
      LOGGER.info("Game loaded");
    } catch (final FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (ois != null) {
        try {
          ois.close();
        } catch (final IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      if (fis != null) {
        try {
          fis.close();
        } catch (final IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
  
  protected void saveGame() {
    final File sav = new File("save"); // TODO
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;
    try {
      if (currentEvent == null || !currentEvent.isLoadSavePoint()) {
        LOGGER.warn("The game cannot be saved right now"); // better notification for user
        return;
      }
      
      fos = new FileOutputStream(sav);
      oos = new ObjectOutputStream(fos);
      StateObject.save(oos);
      IMAGE_AREA.writeExternal(oos);
      TEXT_AREA.writeExternal(oos);
      oos.writeObject(currentEvent);
      
      LOGGER.info("Game saved");
    } catch (final FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (oos != null) {
        try {
          oos.close();
        } catch (final IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      if (fos != null) {
        try {
          fos.close();
        } catch (final IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
  
  protected Event getCurrentEvent() {
    return Main.currentEvent;
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
      currentEvent.execute();
    }
  }
  
  private void logVersion() {
    final InputStream versionInputStream = ClassLoader.getSystemResourceAsStream(PathFinder
        .getLocationOfVerionsFileOnClasspath());
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