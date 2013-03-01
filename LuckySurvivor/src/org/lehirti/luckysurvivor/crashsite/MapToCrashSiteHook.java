package org.lehirti.luckysurvivor.crashsite;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.AbstractEvent;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.hooks.EventHook;
import org.lehirti.engine.state.DateTime;
import org.lehirti.engine.state.State;
import org.lehirti.luckysurvivor.npc.hazel.MeetHazel;
import org.lehirti.luckysurvivor.npc.heidi.MeetHeidi;
import org.lehirti.luckysurvivor.npc.sophia.MeetSophia;

public final class MapToCrashSiteHook implements EventHook {
  static {
    AbstractEvent.registerHook(MapToCrashSite.class, new MapToCrashSiteHook());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent) {
    final Map<Event<?>, Double> events = new HashMap<>();
    
    if (previousEvent.getClass().equals(org.lehirti.luckysurvivor.map.Map.class)) { // is coming from the map
      if (State.getEventCount(SeeAngryDiscussion.class) == 0) { // AND has not seen the angry discussion
        // TODO 100% for testing
        events.put(new SeeAngryDiscussion(), Double.valueOf(100.0));
      }
      
      if (State.getEventCount(MeetHeidi.class) == 0) { // AND has not met heidi
        events.put(new MeetHeidi(), Double.valueOf(20.0));
      }
      
      if (State.getEventCount(MeetHazel.class) == 0) { // AND has not met hazel
        events.put(new MeetHazel(), Double.valueOf(20.0));
      }
      
      if (State.getEventCount(MeetSophia.class) == 0 // AND has not met sophia
          && DateTime.getDDhhmmss() > 3000000) { // AND at least 2 days after crash (crash is day one)
        events.put(new MeetSophia(), Double.valueOf(20.0));
      }
    }
    return events;
  }
}
