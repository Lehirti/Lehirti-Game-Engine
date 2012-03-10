package org.lehirti.events;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.lehirti.Main;
import org.lehirti.gui.Key;
import org.lehirti.res.ResourceCache;
import org.lehirti.res.text.CommonText;
import org.lehirti.res.text.TextKey;
import org.lehirti.res.text.TextWrapper;
import org.lehirti.state.BoolState;
import org.lehirti.state.IntState;
import org.lehirti.state.StateObject;
import org.lehirti.state.StringState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventNode extends AbstractEvent {
  private static final Logger LOGGER = LoggerFactory.getLogger(EventNode.class);
  
  private final ConcurrentMap<Key, Event> registeredEvents = new ConcurrentHashMap<Key, Event>();
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
    
    synchronized (this) {
      while (Main.currentEvent == this) {
        try {
          wait();
        } catch (final InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  }
  
  protected static boolean is(final BoolState key) {
    return StateObject.is(key);
  }
  
  protected static int get(final IntState key) {
    return StateObject.get(key);
  }
  
  protected static String get(final StringState key) {
    return StateObject.get(key);
  }
  
  protected static void set(final BoolState key, final boolean value) {
    StateObject.set(key, value);
  }
  
  protected static void set(final IntState key, final int value) {
    StateObject.set(key, value);
  }
  
  protected static void change(final IntState key, final int change) {
    set(key, get(key) + change);
  }
  
  protected static void set(final StringState key, final String value) {
    StateObject.set(key, value);
  }
  
  @Override
  public boolean handle(final Key key) {
    final Event event = this.registeredEvents.get(key);
    if (event != null) {
      Main.currentEvent = event;
      synchronized (this) {
        notifyAll();
      }
      return true;
    }
    return false;
  }
  
  protected abstract void doEvent();
}
