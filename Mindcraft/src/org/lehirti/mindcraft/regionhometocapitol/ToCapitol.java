package org.lehirti.mindcraft.regionhometocapitol;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.LocationHook;
import org.lehirti.engine.events.StandardEvent;
import org.lehirti.mindcraft.intro.HomeVillage;

public class ToCapitol implements LocationHook {
  static {
    Location.registerDispatcher(HomeVillageToCapitol.class, new ToCapitol());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents() {
    final Map<Event<?>, Double> events = new HashMap<Event<?>, Double>();
    events.put(new StandardEvent(HomeToCapitol.WAY_TO_CAPITOL, HomeToCapitol.WAY_TO_CAPITOL, new HomeVillage()), Double
        .valueOf(100.0));
    return events;
  }
}
