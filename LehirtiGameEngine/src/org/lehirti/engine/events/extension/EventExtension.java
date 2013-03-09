package org.lehirti.engine.events.extension;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.state.StaticInitializer;

public interface EventExtension extends StaticInitializer {
  
  /**
   * this method is called just after e.doEvent()
   * 
   * @param e
   *          the event node which is to be extended. currently only "EventNode" can be extended, not "Event"
   */
  public abstract void doEventExtension(EventNode<?> e);
}
