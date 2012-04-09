package org.lehirti.mindcraft.intro;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.LocationHook;
import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.state.StateObject;
import org.lehirti.mindcraft.C;

public class BathhouseEvents implements LocationHook {
  static {
    Location.registerDispatcher(Bathhouse.class, new BathhouseEvents());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents() {
    final Map<Event<?>, Double> events = new HashMap<Event<?>, Double>();
    if (StateObject.is(Bool.GOT_BLOWJOB_IN_BATHHOUSE)) {
      events.put(new StandardEvent(Intro.THREE_GIRLS_IN_BATHHOUSE, new HomeVillage()), Double.valueOf(100.0));
      if (C.DUCKGIRLS.available) {
        events.put(new StandardEvent(Intro.FUTA_DAY_IN_BATHHOUSE, new HomeVillage()), Double.valueOf(100.0));
      }
    } else {
      if (StateObject.is(Bool.YOU_ARE_HORNY)) {
        events.put(new BathhouseBlowjob(), Double.valueOf(100.0));
      } else {
        events.put(new StandardEvent(Intro.GUYS_ONLY_IN_BATHHOUSE, new HomeVillage()), Double.valueOf(100.0));
      }
    }
    return events;
  }
}
