package crashsite;

import java.util.HashMap;
import java.util.Map;

import lge.events.AbstractEvent;
import lge.events.Event;
import lge.events.SetFlagTextOnlyEvent;
import lge.events.hooks.EventHook;
import lge.gui.Key;
import lge.res.text.TextKey;
import lge.state.State;


public final class UsefullThingsToFindInFuselage implements EventHook {
  static {
    AbstractEvent.registerHook(SearchFuselageForSomethingUsefull.class, new UsefullThingsToFindInFuselage());
  }
  
  public static enum Text implements TextKey {
    FIND_METAL_STRUT,
    FIND_SHEAT_OF_METAL,
    FIND_METAL_POLES,
    FIND_SHEETS_OF_FABRIC,
    FIND_PEANUTS_AND_WATER
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, Event<?> hookedEvent) {
    final Map<Event<?>, Double> availableEvents = new HashMap<>();
    if (!State.is(CrashSiteBoolInventory.METAL_STRUT)) {
      availableEvents.put(new SetFlagTextOnlyEvent(CrashSiteBoolInventory.METAL_STRUT, Key.OPTION_ENTER,
          Text.FIND_METAL_STRUT, new Plane2_Fuselage()), Double.valueOf(20));
    }
    if (!State.is(CrashSiteBoolInventory.SHOVEL)) {
      availableEvents.put(new SetFlagTextOnlyEvent(CrashSiteBoolInventory.SHOVEL, Key.OPTION_ENTER,
          Text.FIND_SHEAT_OF_METAL, new Plane2_Fuselage()), Double.valueOf(20));
    }
    if (!State.is(CrashSiteBoolInventory.LONG_METAL_POLES)) {
      availableEvents.put(new SetFlagTextOnlyEvent(CrashSiteBoolInventory.LONG_METAL_POLES, Key.OPTION_ENTER,
          Text.FIND_METAL_POLES, new Plane2_Fuselage()), Double.valueOf(20));
    }
    if (!State.is(CrashSiteBoolInventory.SHEETS_OF_FABRIC)) {
      availableEvents.put(new SetFlagTextOnlyEvent(CrashSiteBoolInventory.SHEETS_OF_FABRIC, Key.OPTION_ENTER,
          Text.FIND_SHEETS_OF_FABRIC, new Plane2_Fuselage()), Double.valueOf(20));
    }
    if (!State.is(CrashSiteBoolInventory.PEANUTS_AND_BOTTLED_WATER)) {
      availableEvents.put(new SetFlagTextOnlyEvent(CrashSiteBoolInventory.PEANUTS_AND_BOTTLED_WATER, Key.OPTION_ENTER,
          Text.FIND_PEANUTS_AND_WATER, new Plane2_Fuselage()), Double.valueOf(20));
    }
    
    return availableEvents;
  }
}
