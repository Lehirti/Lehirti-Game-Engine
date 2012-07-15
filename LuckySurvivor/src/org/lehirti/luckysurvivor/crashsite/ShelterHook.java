package org.lehirti.luckysurvivor.crashsite;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.AbstractEvent;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.hooks.EventHook;
import org.lehirti.engine.state.DateTime;
import org.lehirti.engine.state.State;
import org.lehirti.luckysurvivor.npc.hazel.MeetHazel;
import org.lehirti.luckysurvivor.npc.hazel.SeeHazelNaked;

public final class ShelterHook implements EventHook {
  static {
    AbstractEvent.registerHook(MapToCrashSite.class, new ShelterHook());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent) {
    final Map<Event<?>, Double> events = new HashMap<Event<?>, Double>();
    
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
