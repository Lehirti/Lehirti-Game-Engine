package org.lehirti.mindcraft.regionhometocapitol;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.AbstractEvent;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.events.hooks.EventHook;
import org.lehirti.engine.gui.Key;
import org.lehirti.mindcraft.intro.HomeVillage;

public class ToCapitol implements EventHook {
  static {
    AbstractEvent.registerHook(HomeVillageToCapitol.class, new ToCapitol());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, Event<?> hookedEvent) {
    final Map<Event<?>, Double> events = new HashMap<>();
    events.put(new StandardEvent(Key.OPTION_ENTER, HomeToCapitol.WAY_TO_CAPITOL, HomeToCapitol.WAY_TO_CAPITOL,
        new HomeVillage()), Double.valueOf(100.0));
    return events;
  }
}
