package org.lehirti.mindcraft.intro;

import java.util.HashMap;
import java.util.Map;

import lge.events.AbstractEvent;
import lge.events.Event;
import lge.events.StandardEvent;
import lge.events.hooks.EventHook;
import lge.gui.Key;
import lge.state.State;

import org.lehirti.mindcraft.C;

public class BathhouseEvents implements EventHook {
  static {
    AbstractEvent.registerHook(Bathhouse.class, new BathhouseEvents());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, Event<?> hookedEvent) {
    final Map<Event<?>, Double> events = new HashMap<>();
    if (State.is(Bool.GOT_BLOWJOB_IN_BATHHOUSE)) {
      events.put(new StandardEvent(Key.OPTION_ENTER, Intro.THREE_GIRLS_IN_BATHHOUSE, Intro.THREE_GIRLS_IN_BATHHOUSE,
          new HomeVillage()), Double.valueOf(100.0));
      if (C.DUCKGIRLS.available) {
        events.put(new StandardEvent(Key.OPTION_ENTER, Intro.FUTA_DAY_IN_BATHHOUSE, Intro.FUTA_DAY_IN_BATHHOUSE,
            new HomeVillage()), Double.valueOf(100.0));
      }
    } else {
      if (State.is(Bool.YOU_ARE_HORNY)) {
        events.put(new BathhouseBlowjob(), Double.valueOf(100.0));
      } else {
        events.put(new StandardEvent(Key.OPTION_ENTER, Intro.GUYS_ONLY_IN_BATHHOUSE, Intro.GUYS_ONLY_IN_BATHHOUSE,
            new HomeVillage()), Double.valueOf(100.0));
      }
    }
    return events;
  }
}
