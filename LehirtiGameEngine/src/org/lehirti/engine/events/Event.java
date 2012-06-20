package org.lehirti.engine.events;

import java.util.Collection;

import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;

public interface Event<STATE extends Enum<?>> {
  public static enum NullState {
  }
  
  public void execute();
  
  public boolean handleKeyEvent(Key key);
  
  /**
   * @return true if and only if this event is in state that it can be saved from
   */
  public boolean isLoadSavePoint();
  
  /**
   * must be implemented by Events that return true to canBeSaved();
   */
  public void resumeFromSavePoint();
  
  /**
   * must be implemented by Events that return true to canBeSaved();
   */
  public void newEventHasBeenLoaded();
  
  /**
   * when adding an event as an option not the event itself, but event.getActualEvent() will be scheduled.
   * 
   * @return this, if there are no EventHooks for this event which inject another event instead
   */
  public Event<?> getActualEvent();
  
  public STATE getEventState();
  
  public void setEventState(STATE newState);
  
  public Collection<ImageKey> getAllUsedImages();
}
