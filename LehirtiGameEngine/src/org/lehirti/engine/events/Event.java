package org.lehirti.engine.events;

import org.lehirti.engine.gui.Key;

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
  
  public STATE getEventState();
  
  public void setEventState(STATE newState);
}
