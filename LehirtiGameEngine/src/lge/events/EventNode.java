package lge.events;

import java.awt.event.KeyEvent;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lge.events.extension.EventExtension;
import lge.gui.EngineMain;
import lge.gui.Key;
import lge.res.ResourceCache;
import lge.res.images.ImageKey;
import lge.res.images.ImageWrapper;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;
import lge.res.text.TextWrapper;
import lge.state.BoolState;
import lge.state.EventState;
import lge.state.IntState;
import lge.state.State;
import lge.state.StringState;
import lge.state.TimeInterval;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventNode<STATE extends Enum<?> & EventState> extends AbstractEvent<STATE> implements
    Externalizable {
  public static final long serialVersionUID = 1L; // yes, public!
  
  private static final Logger LOGGER = LoggerFactory.getLogger(EventNode.class);
  
  private static final Map<Class<? extends EventNode<?>>, Set<EventExtension>> EVENT_EXTENSIONS = new HashMap<>();
  
  public static synchronized void registerExtension(final Class<? extends EventNode<?>> event,
      final EventExtension eventExtension) {
    Set<EventExtension> extensionsPerEvent = EVENT_EXTENSIONS.get(event);
    if (extensionsPerEvent == null) {
      extensionsPerEvent = new HashSet<>();
      EVENT_EXTENSIONS.put(event, extensionsPerEvent);
    }
    extensionsPerEvent.add(eventExtension);
  }
  
  private final ConcurrentMap<Key, Event<?>> registeredEvents = new ConcurrentHashMap<>();
  private transient final List<Key> availableOptionKeys = Key.getOptionKeys();
  private transient final Map<Event<?>, TextWrapper> optionsWithArbitraryKey = new LinkedHashMap<>();
  
  private boolean savePointReached = false;
  private transient boolean newEventHasBeenLoaded = false;
  private boolean isTextInput = false;
  
  /**
   * clear text field and set new text
   * 
   * @param text
   */
  public void setText(final TextWrapper text) {
    EngineMain.getCurrentTextArea().setText(text);
  }
  
  public void setText(final TextKey key) {
    setText(ResourceCache.getNullable(key));
  }
  
  /**
   * append text to text field
   * 
   * @param text
   */
  public void addText(final TextWrapper text) {
    EngineMain.getCurrentTextArea().addText(text);
  }
  
  public void addText(final TextKey key) {
    addText(ResourceCache.getNullable(key));
  }
  
  public void addOption(final Key key, final TextKey text, final Event<?> event) {
    final boolean keyIsAvailable = this.availableOptionKeys.remove(key);
    if (!keyIsAvailable) {
      if (key != null) {
        LOGGER.warn("Requested option key {} not available; using arbitrary option key instead.", key.name());
      }
      addOption(ResourceCache.getNullable(text), event);
      return;
    }
    
    addOption(text, key, event);
  }
  
  public void addOption(final Key key, final TextWrapper text, final Event<?> event) {
    final boolean keyIsAvailable = this.availableOptionKeys.remove(key);
    if (!keyIsAvailable) {
      if (key != null) {
        LOGGER.warn("Requested option key {} not available; using arbitrary option key instead.", key.name());
      }
      addOption(text, event);
      return;
    }
    
    addOption(text, key, event);
  }
  
  public void addOption(final TextKey text, final Event<?> event) {
    addOption(ResourceCache.getNullable(text), event);
  }
  
  public void addOption(final TextWrapper text, final Event<?> event) {
    this.optionsWithArbitraryKey.put(event, text);
  }
  
  /**
   * An event can *EITHER* have regular options *OR* ONE text input option
   * 
   * @param text
   * @param event
   */
  protected void setTextInputOption(final TextKey text, final String initialText, final Event<?> event) {
    this.registeredEvents.put(Key.TEXT_INPUT_OPTION_ENTER, event.getActualEvent(this));
    this.isTextInput = true;
    EngineMain.getCurrentOptionArea().setTextInputOption(text, initialText);
  }
  
  private void addOptionsWithArbitraryKeys() {
    for (final Map.Entry<Event<?>, TextWrapper> entry : this.optionsWithArbitraryKey.entrySet()) {
      final Event<?> event = entry.getKey();
      final TextWrapper text = entry.getValue();
      if (this.availableOptionKeys.isEmpty()) {
        LOGGER.error("No more option keys available; dropping option " + text.getValue());
        continue;
      }
      final Key key = this.availableOptionKeys.remove(0);
      
      addOption(text, key, event);
    }
  }
  
  private void addOption(final TextKey text, final Key key, final Event<?> event) {
    this.registeredEvents.put(key, event.getActualEvent(this));
    EngineMain.getCurrentOptionArea().setOption(text, key);
  }
  
  private void addOption(final TextWrapper text, final Key key, final Event<?> event) {
    this.registeredEvents.put(key, event.getActualEvent(this));
    EngineMain.getCurrentOptionArea().setOption(text, key);
  }
  
  public void execute() {
    LOGGER.info("Event: {}", getClass().getName());
    
    stopBackgroundLoadingOfImages();
    
    if (!this.savePointReached) {
      performImageAreaUpdates();
      
      EngineMain.getCurrentOptionArea().clearOptions();
      
      // must be called before doEvent(); otherwise event hooks don't work properly
      // must be called after performImageAreaUpdates(); otherwise the count would be inconsistent in updateImageArea()
      State.incrementEventCount(this);
      
      doEvent();
      
      doEventExtensions();
      
      addOptionsWithArbitraryKeys();
      
      getRequiredTimeInterval().advance();
    }
    repaintImagesIfNeeded();
    EngineMain.getCurrentOptionArea().repaintIfNeeded();
    EngineMain.getCurrentTextArea().repaintIfNeeded();
    
    backgroundLoadNextImages();
    
    resumeFromSavePoint();
  }
  
  private void doEventExtensions() {
    final Set<EventExtension> eventExtensions;
    synchronized (EventNode.class) {
      eventExtensions = EVENT_EXTENSIONS.get(this.getClass());
    }
    if (eventExtensions != null) {
      for (final EventExtension eventExtension : eventExtensions) {
        eventExtension.doEventExtension(this);
      }
    }
  }
  
  private void performImageAreaUpdates() {
    final ImgChange change = updateImageArea();
    if (change.isUpdateBackground()) {
      setBackgroundImage(change.getBackground());
    }
    if (change.isClearForeground()) {
      setImage(null);
    }
    for (final ImageKey imageToRemove : change.getRemovedFGImages()) {
      removeImage(imageToRemove);
    }
    for (final ImageWrapper image : change.getAddedFGImageWrappers()) {
      addImage(image);
    }
    for (final ImageKey image : change.getAddedFGImages()) {
      addImage(image);
    }
  }
  
  /**
   * to be overwritten by subclasses to update the images display on screen; this method is called before doEvent();
   * <b>this method may be called even if the event is not executed, so don't do anything but return a ImgChange
   * object</b>
   */
  
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  public final Collection<ImageKey> getAllUsedImages() {
    final ImgChange change = updateImageArea();
    final Set<ImageKey> allUsedImages = new HashSet<>();
    if (change.isUpdateBackground()) {
      final ImageKey background = change.getBackground();
      if (background != null) {
        allUsedImages.add(background);
      }
    }
    for (final ImageKey fg : change.getAddedFGImages()) {
      allUsedImages.add(fg);
    }
    return allUsedImages;
  }
  
  private static void stopBackgroundLoadingOfImages() {
    ResourceCache.getImagesToPreload().clear();
  }
  
  private void backgroundLoadNextImages() {
    final BlockingQueue<ImageKey> imagePreloadQueue = ResourceCache.getImagesToPreload();
    for (final Event<?> possibleNextEvent : this.registeredEvents.values()) {
      imagePreloadQueue.addAll(possibleNextEvent.getAllUsedImages());
    }
  }
  
  @Override
  public synchronized boolean isLoadSavePoint() {
    return this.savePointReached;
  }
  
  @Override
  public synchronized void newEventHasBeenLoaded() {
    this.newEventHasBeenLoaded = true;
  }
  
  @Override
  public void resumeFromSavePoint() {
    this.savePointReached = true;
    LOGGER.debug("Savepoint reached.");
    synchronized (this) {
      while (EngineMain.getCurrentEvent() == this) {
        try {
          wait();
          if (this.newEventHasBeenLoaded) {
            EngineMain.getCurrentEvent().resumeFromSavePoint();
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
      final Key key = Key.read(in);
      final Event<?> event = (Event<?>) in.readObject();
      this.registeredEvents.put(key, event);
      this.availableOptionKeys.remove(key);
    }
    this.savePointReached = in.readBoolean();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeInt(this.registeredEvents.size());
    for (final Map.Entry<Key, Event<?>> entry : this.registeredEvents.entrySet()) {
      entry.getKey().write(out);
      out.writeObject(entry.getValue());
    }
    out.writeBoolean(this.savePointReached);
  }
  
  protected static boolean is(final BoolState key) {
    return State.is(key);
  }
  
  protected static long get(final IntState key) {
    return State.get(key);
  }
  
  protected static String get(final StringState key) {
    return State.get(key);
  }
  
  protected static void set(final BoolState key, final boolean value) {
    State.set(key, value);
  }
  
  protected static void set(final IntState key, final long value) {
    State.set(key, value);
  }
  
  protected static void change(final IntState key, final long change) {
    set(key, get(key) + change);
  }
  
  protected static void set(final StringState key, final String value) {
    State.set(key, value);
  }
  
  @Override
  public boolean handleKeyEvent(final Key key) {
    final Event<?> event = this.registeredEvents.get(key);
    if (event != null) {
      keyPressed(key);
      EngineMain.setCurrentEvent(event);
      synchronized (this) {
        notifyAll();
      }
      return true;
    }
    return false;
  }
  
  @Override
  public boolean handleKeyEvent(final KeyEvent e, final Key key) {
    if (this.isTextInput) {
      if (key == Key.TEXT_INPUT_OPTION_ENTER) {
        final String finalTextInput = EngineMain.getCurrentOptionArea().getCurrentTextInput();
        if (handleTextInput(finalTextInput)) {
          return handleKeyEvent(Key.TEXT_INPUT_OPTION_ENTER);
        } else {
          return false;
        }
      } else {
        return EngineMain.getCurrentOptionArea().handleKeyEvent(e);
      }
    } else {
      // event has no text input option
      return false;
    }
  }
  
  /**
   * supposed to be overwritten by subclasses, to validate and handle text input by user
   * 
   * @param finalTextInput
   * @return true, if input is valid and game can continue with next event
   */
  protected boolean handleTextInput(final String finalTextInput) {
    return true;
  }
  
  /**
   * callback for event classes to react to (state changes) user input to the own event
   */
  @Override
  public void keyPressed(final Key key) {
    // empty in this base class
  }
  
  protected abstract void doEvent();
  
  public TimeInterval getRequiredTimeInterval() {
    return TimeInterval.noAdvance(); // by default, events do not advance time at all
  }
}
