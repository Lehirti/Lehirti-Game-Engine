package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.lehirti.engine.Main;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.state.BoolState;
import org.lehirti.engine.state.IntState;
import org.lehirti.engine.state.StateObject;
import org.lehirti.engine.state.StringState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventNode extends AbstractEvent implements Externalizable {
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LoggerFactory.getLogger(EventNode.class);
  
  private final ConcurrentMap<Key, Event> registeredEvents = new ConcurrentHashMap<Key, Event>();
  private transient final List<Key> availableOptionKeys = Key.getOptionKeys();
  private transient final Map<Event, TextKey> optionsWithArbitraryKey = new LinkedHashMap<Event, TextKey>();
  
  private transient boolean canBeSaved = false;
  private transient boolean newEventHasBeenLoaded = false;
  
  /**
   * clear text field and set new text
   * 
   * @param text
   */
  private void setText(final TextWrapper text) {
    try {
      javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          Main.TEXT_AREA.setText(text);
        }
      });
    } catch (final InterruptedException e) {
      LOGGER.error("Thread " + Thread.currentThread().toString() + " has been interrupted; terminating thread", e);
      throw new ThreadDeath();
    } catch (final InvocationTargetException e) {
      LOGGER.error("InvocationTargetException trying to set text to text area", e);
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
  private void addText(final TextWrapper text) {
    try {
      javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          Main.TEXT_AREA.addText(text);
        }
      });
    } catch (final InterruptedException e) {
      LOGGER.error("Thread " + Thread.currentThread().toString() + " has been interrupted; terminating thread", e);
      throw new ThreadDeath();
    } catch (final InvocationTargetException e) {
      LOGGER.error("InvocationTargetException trying to add text to text area", e);
    }
  }
  
  protected void addText(final TextKey key) {
    addText(ResourceCache.get(key));
  }
  
  protected void addOption(final Key key, final TextKey text, final Event event) {
    final boolean keyIsAvailable = this.availableOptionKeys.remove(key);
    if (!keyIsAvailable) {
      LOGGER.warn("Requested option key {} not available; using arbitrary option key instead.", key.name());
      addOption(text, event);
      return;
    }
    
    this.registeredEvents.put(key, event);
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
        LOGGER.error("No more option keys available; dropping option " + ResourceCache.get(text).getValue());
        continue;
      }
      final Key key = this.availableOptionKeys.remove(0);
      this.registeredEvents.put(key, event);
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
    
    resumeFromSavePoint();
  }
  
  @Override
  public synchronized boolean isLoadSavePoint() {
    return this.canBeSaved;
  }
  
  @Override
  public synchronized void newEventHasBeenLoaded() {
    this.newEventHasBeenLoaded = true;
  }
  
  @Override
  public void resumeFromSavePoint() {
    this.canBeSaved = true;
    LOGGER.debug("Savepoint reached.");
    synchronized (this) {
      while (Main.currentEvent == this) {
        try {
          wait();
          if (this.newEventHasBeenLoaded) {
            Main.currentEvent.resumeFromSavePoint();
          }
        } catch (final InterruptedException e) {
          LOGGER.error("Thread " + Thread.currentThread().toString() + " has been interrupted; terminating thread", e);
          throw new ThreadDeath();
        }
      }
    }
    cleanUp();
  }
  
  /**
   * can be overwritten by subclasses to do clean up work after the event is over and user input has been processed
   */
  protected void cleanUp() {
    // empty in this base class
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    final int nrOfEvents = in.readInt();
    for (int i = 0; i < nrOfEvents; i++) {
      final String name = (String) in.readObject();
      final Key key = Key.valueOf(name);
      final Event event = (Event) in.readObject();
      this.registeredEvents.put(key, event);
    }
    
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeInt(this.registeredEvents.size());
    for (final Map.Entry<Key, Event> entry : this.registeredEvents.entrySet()) {
      out.writeObject(entry.getKey().name());
      out.writeObject(entry.getValue());
    }
  }
  
  protected static boolean is(final BoolState key) {
    return StateObject.is(key);
  }
  
  protected static long get(final IntState key) {
    return StateObject.get(key);
  }
  
  protected static String get(final StringState key) {
    return StateObject.get(key);
  }
  
  protected static void set(final BoolState key, final boolean value) {
    StateObject.set(key, value);
  }
  
  protected static void set(final IntState key, final long value) {
    StateObject.set(key, value);
  }
  
  protected static void change(final IntState key, final long change) {
    set(key, get(key) + change);
  }
  
  protected static void set(final StringState key, final String value) {
    StateObject.set(key, value);
  }
  
  @Override
  public boolean handleKeyEvent(final Key key) {
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
