package crashsite;

import java.util.HashMap;
import java.util.Map;

import lge.events.AbstractEvent;
import lge.events.Event;
import lge.events.hooks.EventHook;
import lge.state.DateTime;
import lge.state.State;

import npc.hazel.MeetHazel;
import npc.hazel.SeeHazelNaked;

public final class ShelterHook implements EventHook {
  static {
    AbstractEvent.registerHook(MapToCrashSite.class, new ShelterHook());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, Event<?> hookedEvent) {
    final Map<Event<?>, Double> events = new HashMap<>();
    
    if (!previousEvent.getClass().equals(Shelter.class)) { // is entering shelter (not already inside)
      if (State.getEventCount(MeetHazel.class) != 0 // AND has met hazel
          && (DateTime.gethhmmss() >= 220000 || DateTime.gethhmmss() <= 70000)) { // AND time in [22:00:00-07:00:00]
        // TODO no longer show event when girls have moved to hut
        events.put(new SeeHazelNaked(), Double.valueOf(20.0));
      }
    }
    return events;
  }
}
