package org.lehirti.events;

import java.lang.reflect.InvocationTargetException;

import org.lehirti.Main;
import org.lehirti.res.ResourceCache;
import org.lehirti.res.images.ImageKey;
import org.lehirti.res.images.ImageWrapper;

public abstract class AbstractEvent implements Event {
  protected void setImage(final ImageKey key) {
    final ImageWrapper image = ResourceCache.get(key);
    try {
      javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          Main.IMAGE_AREA.setImage(image);
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
  
  // TODO
  protected void setBackgroundImage(final ImageKey key) {
    final ImageWrapper image = ResourceCache.get(key);
    try {
      javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          Main.IMAGE_AREA.setBackgroundImage(image);
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
