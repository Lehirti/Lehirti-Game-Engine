package org.atrun.intro.events;

import java.util.HashMap;
import java.util.Map;

import org.atrun.locations.DrostalToVogard;
import org.lehirti.events.Event;
import org.lehirti.events.Location;
import org.lehirti.events.LocationHook;

public class DrostalToVogardHook implements LocationHook {
  static {
    Location.registerDispatcher(DrostalToVogard.class, new DrostalToVogardHook());
  }
  
  @Override
  public Map<Event, Double> getCurrentEvents() {
    final Map<Event, Double> map = new HashMap<Event, Double>();
    map.put(new Intro04(), Double.valueOf(50));
    return map;
  }
}
