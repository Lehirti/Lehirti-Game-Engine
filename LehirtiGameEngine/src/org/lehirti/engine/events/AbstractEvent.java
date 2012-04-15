package org.lehirti.engine.events;

import java.lang.reflect.InvocationTargetException;

import org.lehirti.engine.Main;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.state.StateObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEvent<STATE extends Enum<?>> implements Event<STATE> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEvent.class);
  
  private boolean repaintNeeded = false;
  
  protected void setImage(final ImageKey key) {
    Main.IMAGE_AREA.setImage(key);
    this.repaintNeeded = true;
  }
  
  protected void addImage(final ImageKey key) {
    Main.IMAGE_AREA.addImage(key);
    this.repaintNeeded = true;
  }
  
  protected void setBackgroundImage(final ImageKey key) {
    Main.IMAGE_AREA.setBackgroundImage(key);
    this.repaintNeeded = true;
  }
  
  protected void removeImage(final ImageKey imageKey) {
    Main.IMAGE_AREA.removeImage(imageKey);
    this.repaintNeeded = true;
  }
  
  protected void repaintImagesIfNeeded() {
    if (this.repaintNeeded) {
      try {
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            Main.IMAGE_AREA.repaint();
          }
        });
      } catch (final InterruptedException e) {
        LOGGER.error("Thread " + Thread.currentThread().toString() + " has been interrupted; terminating thread", e);
        throw new ThreadDeath();
      } catch (final InvocationTargetException e) {
        LOGGER.error("InvocationTargetException trying to repaint image area", e);
      }
    }
  }
  
  @Override
  public boolean handleKeyEvent(final Key key) {
    return false;
  }
  
  @Override
  public boolean isLoadSavePoint() {
    return false;
  }
  
  @Override
  public void resumeFromSavePoint() {
    throw new UnsupportedOperationException(
        "Event "
            + this.getClass().getName()
            + " has no save point from which it could resume. It should never have been saved. This is a program bug. We apologize for the inconvenience.");
  }
  
  @Override
  public void newEventHasBeenLoaded() {
    throw new UnsupportedOperationException(
        "Event "
            + this.getClass().getName()
            + " is not in a state from which it can resume a newly loaded game. This is a program bug. We apologize for the inconvenience.");
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public STATE getEventState() {
    return (STATE) StateObject.get(getClass());
  }
  
  @Override
  public void setEventState(final STATE newState) {
    StateObject.set(getClass(), newState);
    
  }
}
