package org.lehirti.events;

import java.lang.reflect.InvocationTargetException;

import org.lehirti.Main;
import org.lehirti.gui.Key;
import org.lehirti.res.images.ImageKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEvent implements Event {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEvent.class);
  
  private boolean repaintNeeded = false;
  
  protected void setImage(final ImageKey key) {
    Main.IMAGE_AREA.setImage(key);
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
}
