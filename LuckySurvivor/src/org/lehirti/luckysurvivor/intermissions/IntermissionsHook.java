package org.lehirti.luckysurvivor.intermissions;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.AbstractEvent;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.hooks.EventHook;
import org.lehirti.engine.state.DateTime;
import org.lehirti.engine.state.State;
import org.lehirti.luckysurvivor.common.Rest;

public final class IntermissionsHook implements EventHook {
  static {
    AbstractEvent.registerHook(Rest.class, new IntermissionsHook());
  }
  
  private static final int[] INTERMISSION_AFTER_DAYS = new int[Intermission.TxtImg.values().length];
  static {
    int i = 0;
    for (final Intermission.TxtImg intermission : Intermission.TxtImg.values()) {
      INTERMISSION_AFTER_DAYS[i] = intermission.afterDays;
      i++;
    }
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, final Event<?> hookedEvent) {
    final Map<Event<?>, Double> availableEvents = new HashMap<>();
    try {
      final int afterDays = INTERMISSION_AFTER_DAYS[State.getEventCount(Intermission.class)];
      if (DateTime.getDay() > afterDays) {
        availableEvents.put(new Intermission(hookedEvent), EventHook.PROBABILITY_DEFAULT);
      }
    } catch (final ArrayIndexOutOfBoundsException ignore) {
      // no more Intermissions
    }
    
    return availableEvents;
  }
}
