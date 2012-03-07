package org.lehirti.events;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lehirti.Main;
import org.lehirti.gui.Key;
import org.lehirti.res.ResourceCache;
import org.lehirti.res.text.TextKey;

public abstract class EventNode extends AbstractEvent {
  private final Map<Key, Event> registeredEvents = new EnumMap<Key, Event>(Key.class);
  private final List<Key> availableOptionKeys = Key.getOptionKeys();
  private final Map<Event, TextKey> optionsWithArbitraryKey = new LinkedHashMap<Event, TextKey>();
  
  /**
   * clear text field and set new text
   * 
   * @param text
   */
  protected void setText(final TextKey text) {
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
  protected void addText(final TextKey text) {
    try {
      javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          Main.TEXT_AREA.addText(text);
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
  
  private void addText(final String text) {
    try {
      javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          Main.TEXT_AREA.setText(Main.TEXT_AREA.getText() + text);
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
  
  protected void addOption(final Key key, final TextKey text, final Event event) {
    final boolean keyIsAvailable = this.availableOptionKeys.remove(key);
    if (!keyIsAvailable) {
      // TODO LOG warning
      addOption(text, event);
      return;
    }
    
    this.registeredEvents.put(key, event);
    addText("\n" + key.mapping + " : " + ResourceCache.get(text).getValue());
  }
  
  protected void addOption(final TextKey text, final Event event) {
    this.optionsWithArbitraryKey.put(event, text);
  }
  
  private void addOptionsWithAritraryKeys() {
    for (final Map.Entry<Event, TextKey> entry : this.optionsWithArbitraryKey.entrySet()) {
      final Event event = entry.getKey();
      final TextKey text = entry.getValue();
      if (this.availableOptionKeys.isEmpty()) {
        // TODO LOG error
        continue;
      }
      final Key key = this.availableOptionKeys.remove(0);
      this.registeredEvents.put(key, event);
      addText("\n" + key.mapping + " : " + ResourceCache.get(text).getValue());
    }
  }
  
  public void execute() {
    doEvent();
    
    addOptionsWithAritraryKeys();
    
    repaintImagesIfNeeded();
    
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
