package org.lehirti.luckysurvivor.crashsite;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.AbstractEvent;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.hooks.EventHook;
import org.lehirti.engine.state.State;

public final class SeeAngryDiscussionHook implements EventHook {
  static {
    AbstractEvent.registerHook(MapToCrashSite.class, new SeeAngryDiscussionHook());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent) {
    final Map<Event<?>, Double> events = new HashMap<Event<?>, Double>();
    
    if (State.getEventCount(MapToCrashSite.class) > 0 // has been to crash site before (redundant)
        && previousEvent.getClass().equals(org.lehirti.luckysurvivor.map.Map.class) // AND is coming from the map
        && State.getEventCount(SeeAngryDiscussion.class) == 0) { // AND has not seen the angry discussion
      events.put(new SeeAngryDiscussion(), Double.valueOf(100.0));
      /*
       * I Like the idea with the %! 100%for testing
       */
    }
    return events;
  }
}
