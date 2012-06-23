package org.lehirti.mindcraft.intro;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.EventHook;
import org.lehirti.engine.state.State;

public class NeighbourEvents implements EventHook {
  static {
    Location.registerDispatcher(Neighbour.class, new NeighbourEvents());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(Event<?> previousEvent) {
    final Map<Event<?>, Double> events = new HashMap<Event<?>, Double>();
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
