package org.lehirti.luckysurvivor.peninsulabasinjungle;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.AbstractEvent;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.LocationHook;
import org.lehirti.engine.state.StateObject;

public final class YurikaVineRapeHook implements LocationHook {
  static {
    AbstractEvent.registerDispatcher(MapToPeninsulaBasinJungle.class, new YurikaVineRapeHook());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents() {
    final Map<Event<?>, Double> events = new HashMap<Event<?>, Double>();
    
    // this is how you inject a one-time event (in this case into MapToPeninsulaBasinJungle)
    if (StateObject.getEventCount(YurikaVineRape.class) == 0) {
      events.put(new YurikaVineRape(), Double.valueOf(LocationHook.PROBABILITY_ALWAYS));
    }
    
    return events;
  }
  
}
