package org.lehirti.luckysurvivor.pondoverhang;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.AbstractEvent;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.hooks.EventHook;
import org.lehirti.engine.state.State;

public final class GirlsPresentHook implements EventHook {
  static {
    AbstractEvent.registerHook(MapToPondOverhang.class, new GirlsPresentHook());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent) {
    final Map<Event<?>, Double> events = new HashMap<Event<?>, Double>();
    
    /*
     * only allow the GirlsPresent event, if GIRLS_PRESENT is false, otherwise it would be possible to get the
     * GirlsPresent Event multiple times in a row (without getting to the MapToPondOverhang event in between).
     */
    if (!State.is(PondOverhangBool.GIRLS_PRESENT)) {
      events.put(new GirlsPresent(), Double.valueOf(25.0));
    }
    
    return events;
  }
}
