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
import org.lehirti.state.StateObject;
import org.lehirti.state.StaticInitializer;
import org.lehirti.util.ClassFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Main {
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  
  private static final String VERSION_FILE_LOCATION = "version";
  
  public static TextArea TEXT_AREA;
  public static ImageArea IMAGE_AREA;
  
  public static volatile Event currentEvent = null;
  
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
        try {
          final Key key = Key.getByChar(e.getKeyChar());
          
          if (key == null) {
            // key unrelated to the game pressed
            return;
          }
          
          // let current event handle key event first
          if (getCurrentEvent() != null) {
            if (getCurrentEvent().handle(key)) {
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
      fis = new FileInputStream(sav);
      ois = new ObjectInputStream(fis);
      StateObject.load(ois);
    } catch (final FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final IOException e) {
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
      fos = new FileOutputStream(sav);
      oos = new ObjectOutputStream(fos);
      StateObject.save(oos);
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