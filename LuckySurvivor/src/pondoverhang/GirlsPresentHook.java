package pondoverhang;

import java.util.HashMap;
import java.util.Map;

import lge.events.AbstractEvent;
import lge.events.Event;
import lge.events.hooks.EventHook;
import lge.state.State;


public final class GirlsPresentHook implements EventHook {
  static {
    AbstractEvent.registerHook(MapToPondOverhang.class, new GirlsPresentHook());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, Event<?> hookedEvent) {
    final Map<Event<?>, Double> events = new HashMap<>();
    
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
