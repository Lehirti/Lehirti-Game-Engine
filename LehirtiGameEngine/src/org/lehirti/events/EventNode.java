package org.lehirti.events;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lehirti.Main;
import org.lehirti.gui.Key;
import org.lehirti.res.ResourceCache;
import org.lehirti.res.text.CommonText;
import org.lehirti.res.text.TextKey;
import org.lehirti.res.text.TextWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventNode extends AbstractEvent {
  private static final Logger LOGGER = LoggerFactory.getLogger(EventNode.class);
  
  private final Map<Key, Event> registeredEvents = new EnumMap<Key, Event>(Key.class);
  private final List<Key> availableOptionKeys = Key.getOptionKeys();
  private final Map<Event, TextKey> optionsWithArbitraryKey = new LinkedHashMap<Event, TextKey>();
  
  /**
   * clear text field and set new text
   * 
   * @param text
   */
  protected void setText(final TextWrapper text) {
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
  
  protected void setText(final TextKey key) {
    setText(ResourceCache.get(key));
  }
  
  /**
   * append text to text field
   * 
   * @param text
   */
  protected void addText(final TextWrapper text) {
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
  
  protected void addText(final TextKey key) {
    addText(ResourceCache.get(key));
  }
  
  protected void addOption(final Key key, final TextKey text, final Event event) {
    final boolean keyIsAvailable = this.availableOptionKeys.remove(key);
    if (!keyIsAvailable) {
      // TODO LOG warning
      addOption(text, event);
      return;
    }
    
    this.registeredEvents.put(key, event);
    // TODO addText("\n" + key.mapping + " : " + ResourceCache.get(text).getValue());
    final TextWrapper wrapper = ResourceCache.get(CommonText.KEY_OPTION);
    wrapper.addParameter(String.valueOf(key.mapping));
    addText(wrapper);
    addText(text);
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
      // TODO addText("\n" + key.mapping + " : " + ResourceCache.get(text).getValue());
      final TextWrapper wrapper = ResourceCache.get(CommonText.KEY_OPTION);
      wrapper.addParameter(String.valueOf(key.mapping));
      addText(wrapper);
      addText(text);
    }
  }
  
  public void execute() {
    LOGGER.info("Event: {}", getClass().getName());
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
