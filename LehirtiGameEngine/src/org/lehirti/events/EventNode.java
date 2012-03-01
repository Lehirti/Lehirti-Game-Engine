package org.lehirti.events;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.Map;

import org.lehirti.Key;
import org.lehirti.Main;
import org.lehirti.res.images.ImageCache;
import org.lehirti.res.images.ImageKey;

public abstract class EventNode implements Event {
  private final Map<Key, Event> registeredEvents = new EnumMap<Key, Event>(Key.class);
  
  /**
   * TODO clear text field set text in text field
   * 
   * @param text
   */
  protected void setText(final String text) {
    try {
      javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          Main.TEXT_AREA.setText(text);
        }
      });
    } catch (final InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /**
   * append text to text field
   * 
   * @param text
   */
  protected void addText(final String text) {
    try {
      javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          final String oldText = Main.TEXT_AREA.getText();
          Main.TEXT_AREA.setText(oldText + "\n" + text);
        }
      });
    } catch (final InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  protected void setImage(final ImageKey key) {
    final BufferedImage image = ImageCache.getImage(key).getRandomImage();
    try {
      javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          Main.IMAGE_AREA.setImage(image);
          Main.IMAGE_AREA.repaint();
        }
      });
    } catch (final InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  
  protected void setInputOption(final Key key, final Event event) {
    this.registeredEvents.put(key, event);
    addText(key.c + ": " + key.toString());
  }
  
  public void execute() {
    doEvent();
    Event event = null;
    while (true) {
      Key key;
      synchronized (Main.LAST_KEY_TYPED_LOCK) {
        while ((key = Main.LAST_KEY_TYPED) == null) {
          try {
            Main.LAST_KEY_TYPED_LOCK.wait();
          } catch (final InterruptedException e) {
            e.printStackTrace();
          }
        }
        Main.LAST_KEY_TYPED = null;
      }
      
      event = this.registeredEvents.get(key);
      if (event != null) {
        break;
      }
    }
    Main.nextEvent = event;
  }
  
  protected abstract void doEvent();
}
