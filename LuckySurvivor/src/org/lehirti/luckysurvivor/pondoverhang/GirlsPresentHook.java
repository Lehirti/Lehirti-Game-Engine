package org.lehirti.luckysurvivor.pondoverhang;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.AbstractEvent;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.hooks.EventHook;


public final class GirlsPresentHook implements EventHook {
  static {
    AbstractEvent.registerHook(MapToPondOverhang.class, new GirlsPresentHook());
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent) {
    final Map<Event<?>, Double> events = new HashMap<Event<?>, Double>();
   
      events.put(new GirlsPresent(), Double.valueOf(25.0));    
      
    return events;
  }
}
