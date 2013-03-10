package org.lehirti.mindcraft.intro;

import java.util.HashMap;
import java.util.Map;

import lge.events.AbstractEvent;
import lge.events.Event;
import lge.events.hooks.EventHook;
import lge.state.State;


public class NeighbourEvents implements EventHook {
  static {
    AbstractEvent.registerHook(Neighbour.class, new NeighbourEvents());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, Event<?> hookedEvent) {
    final Map<Event<?>, Double> events = new HashMap<>();
    if (State.get(MeetDitaOutsideHerHouse.class) == null) {
      events.put(new MeetDitaOutsideHerHouse(), Double.valueOf(100.0d));
    } else if (State.is(Bool.GOT_BLOWJOB_IN_BATHHOUSE) && State.is(Bool.MARKET_SELLER_FUCKED)) {
      events.put(new DitaFlees(), Double.valueOf(100.0d));
    } else if (State.is(Bool.GOT_BLOWJOB_IN_BATHHOUSE) || State.is(Bool.MARKET_SELLER_FUCKED)) {
      events.put(new DitaIsAroused(), Double.valueOf(100.0d));
    } else {
      events.put(new MeetDitaOutsideHerHouse(), Double.valueOf(100.0d));
    }
    return events;
  }
}
