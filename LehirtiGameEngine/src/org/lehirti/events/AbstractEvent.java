package org.lehirti.events;

import java.lang.reflect.InvocationTargetException;

import org.lehirti.Main;
import org.lehirti.res.images.ImageKey;

public abstract class AbstractEvent implements Event {
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
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (final InvocationTargetException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
