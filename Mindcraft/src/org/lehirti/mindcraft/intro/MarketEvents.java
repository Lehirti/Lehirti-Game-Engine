package org.lehirti.mindcraft.intro;

import java.util.HashMap;
import java.util.Map;

import lge.events.AbstractEvent;
import lge.events.Event;
import lge.events.hooks.EventHook;
import lge.state.State;


public class MarketEvents implements EventHook {
  static {
    AbstractEvent.registerHook(Market.class, new MarketEvents());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, Event<?> hookedEvent) {
    final Map<Event<?>, Double> events = new HashMap<>();
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
