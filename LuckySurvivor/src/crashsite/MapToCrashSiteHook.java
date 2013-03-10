package crashsite;

import java.util.HashMap;
import java.util.Map;

import lge.events.AbstractEvent;
import lge.events.Event;
import lge.events.hooks.EventHook;
import lge.state.DateTime;
import lge.state.State;

import npc.hazel.MeetHazel;
import npc.heidi.MeetHeidi;
import npc.sophia.MeetSophia;

public final class MapToCrashSiteHook implements EventHook {
  static {
    AbstractEvent.registerHook(MapToCrashSite.class, new MapToCrashSiteHook());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, Event<?> hookedEvent) {
    final Map<Event<?>, Double> events = new HashMap<>();
    
    if (previousEvent.getClass().equals(map.Map.class)) { // is coming from the map
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
