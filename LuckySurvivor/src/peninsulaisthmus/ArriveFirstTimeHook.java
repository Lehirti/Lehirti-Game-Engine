package peninsulaisthmus;

import java.util.HashMap;
import java.util.Map;

import lge.events.AbstractEvent;
import lge.events.Event;
import lge.events.hooks.EventHook;
import lge.state.State;


public final class ArriveFirstTimeHook implements EventHook {
  static {
    AbstractEvent.registerHook(MapToPeninsulaIsthmus.class, new ArriveFirstTimeHook());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, Event<?> hookedEvent) {
    final Map<Event<?>, Double> events = new HashMap<>();
    
    // first time you come to MapToPeninsulaIsthmus, the ArriveFirstTime one-time event will show instead
    if (State.getEventCount(ArriveFirstTime.class) == 0) {
      events.put(new ArriveFirstTime(), EventHook.PROBABILITY_ALWAYS);
    }
    return events;
  }
}
