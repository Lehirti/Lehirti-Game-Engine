package org.lehirti.mindcraft.intro;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.AbstractEvent;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.hooks.EventHook;
import org.lehirti.engine.state.State;

public class MarketEvents implements EventHook {
  static {
    AbstractEvent.registerHook(Market.class, new MarketEvents());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent) {
    final Map<Event<?>, Double> events = new HashMap<Event<?>, Double>();
    if (State.is(Bool.YOU_ARE_HORNY)) {
      if (!State.is(Bool.MARKET_SELLER_FUCKED)) {
        events.put(new FuckMarketSeller(), Double.valueOf(50.0d));
      }
      events.put(new FuckRandomMarketGoer(), Double.valueOf(100.0d));
    } else {
      events.put(new MeetRandomMarketGoer(), Double.valueOf(100.0d));
    }
    return events;
  }
}
