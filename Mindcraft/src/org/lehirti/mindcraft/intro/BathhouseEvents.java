package org.lehirti.mindcraft.intro;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.LocationHook;
import org.lehirti.engine.state.StateObject;

public class BathhouseEvents implements LocationHook {
  static {
    Location.registerDispatcher(Bathhouse.class, new BathhouseEvents());
  }
  
  @Override
  public Map<Event, Double> getCurrentEvents() {
    final Map<Event, Double> events = new HashMap<Event, Double>();
    if (StateObject.is(Bool.GOT_BLOWJOB_IN_BATHHOUSE)) {
      events.put(new BathhouseThreeGirls(), Double.valueOf(100.0));
    } else {
      if (StateObject.is(Bool.HAS_MASTURBATED_IN_THE_MORNING)) {
        events.put(new BathhouseGuysOnly(), Double.valueOf(100.0));
      } else {
        events.put(new BathhouseBlowjob(), Double.valueOf(100.0));
      }
    }
    return events;
  }
}
