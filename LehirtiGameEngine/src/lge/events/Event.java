package lge.events;

import java.awt.event.KeyEvent;
import java.util.Collection;

import lge.gui.Key;
import lge.res.images.ImageKey;
import lge.state.EventState;
import lge.state.TimeInterval;


public interface Event<STATE extends Enum<?>> {
  public static enum NullState implements EventState {
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
   * @param previousEvent
   *          the event that is adding the option
   * 
   * @return this, if there are no EventHooks for this event which inject another event instead
   */
  public Event<?> getActualEvent(Event<?> previousEvent);
  
  public STATE getEventState();
  
  public void setEventState(STATE newState);
  
  public Collection<ImageKey> getAllUsedImages();
  
  public TimeInterval getRequiredTimeInterval();
  
  public boolean handleKeyEvent(KeyEvent e, Key key);
  
  public void keyPressed(Key key);
}
