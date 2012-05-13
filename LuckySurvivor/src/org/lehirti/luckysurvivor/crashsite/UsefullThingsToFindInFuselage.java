package org.lehirti.luckysurvivor.crashsite;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.LocationHook;
import org.lehirti.engine.events.SetFlagEvent;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.StateObject;

public final class UsefullThingsToFindInFuselage implements LocationHook {
  static {
    Location.registerDispatcher(SearchFuselageForSomethingUsefull.class, new UsefullThingsToFindInFuselage());
  }
  
  public static enum Text implements TextKey {
    FIND_METAL_STRUT,
    FIND_SHEAT_OF_METAL,
    FIND_METAL_POLES,
    FIND_SHEETS_OF_FABRIC,
    FIND_PEANUTS_AND_WATER
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents() {
    final Map<Event<?>, Double> availableEvents = new HashMap<Event<?>, Double>();
    if (!StateObject.is(Bool.HAS_METAL_STRUT)) {
      availableEvents.put(new SetFlagEvent(Bool.HAS_METAL_STRUT, Text.FIND_METAL_STRUT, new Plane2_Fuselage()), Double
          .valueOf(20));
    }
    if (!StateObject.is(Bool.HAS_SHOVEL)) {
      availableEvents.put(new SetFlagEvent(Bool.HAS_SHOVEL, Text.FIND_SHEAT_OF_METAL, new Plane2_Fuselage()), Double
          .valueOf(20));
    }
    if (!StateObject.is(Bool.HAS_LONG_METAL_POLES)) {
      availableEvents.put(new SetFlagEvent(Bool.HAS_LONG_METAL_POLES, Text.FIND_METAL_POLES, new Plane2_Fuselage()),
          Double.valueOf(20));
    }
    if (!StateObject.is(Bool.HAS_SHEETS_OF_FABRIC)) {
      availableEvents.put(
          new SetFlagEvent(Bool.HAS_SHEETS_OF_FABRIC, Text.FIND_SHEETS_OF_FABRIC, new Plane2_Fuselage()), Double
              .valueOf(20));
    }
    if (!StateObject.is(Bool.HAS_PEANUTS_AND_BOTTLED_WATER)) {
      availableEvents.put(new SetFlagEvent(Bool.HAS_PEANUTS_AND_BOTTLED_WATER, Text.FIND_PEANUTS_AND_WATER,
          new Plane2_Fuselage()), Double.valueOf(20));
    }
    
    return availableEvents;
  }
}
