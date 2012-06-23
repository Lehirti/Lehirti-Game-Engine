package org.atrun.intro.events;

import java.util.HashMap;
import java.util.Map;

import org.atrun.locations.DrostalToVogard;
import org.atrun.maze.events.EnterMaze;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.EventHook;

public class DrostalToVogardHook implements EventHook {
  static {
    Location.registerDispatcher(DrostalToVogard.class, new DrostalToVogardHook());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(Event<?> previousEvent) {
    final Map<Event<?>, Double> map = new HashMap<Event<?>, Double>();
    map.put(new Intro04(), Double.valueOf(50));
    map.put(new EnterMaze(), Double.valueOf(50));
    return map;
  }
}
