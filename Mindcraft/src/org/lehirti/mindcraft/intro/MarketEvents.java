package org.lehirti.mindcraft.intro;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.LocationHook;
import org.lehirti.engine.state.StateObject;

public class MarketEvents implements LocationHook {
  static {
    Location.registerDispatcher(Market.class, new MarketEvents());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents() {
    final Map<Event<?>, Double> events = new HashMap<Event<?>, Double>();
    if (StateObject.is(Bool.YOU_ARE_HORNY)) {
      if (!StateObject.is(Bool.MARKET_SELLER_FUCKED)) {
        events.put(new FuckMarketSeller(), Double.valueOf(50.0d));
      }
      events.put(new FuckRandomMarketGoer(), Double.valueOf(100.0d));
    } else {
      events.put(new MeetRandomMarketGoer(), Double.valueOf(100.0d));
    }
    return events;
  }
}
