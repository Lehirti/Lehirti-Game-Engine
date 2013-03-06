package org.lehirti.engine.events.hooks;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.state.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This EventHook allows you to add an arbitrary amount of one-time events to one or more other events.
 * <p/>
 * <b>IMPORTANT: the one-time events must have a standard (no argument) constructor (which will be used to create an
 * instance)</b>
 */
public class OneTimeEventHook implements EventHook {
  private static final Logger LOGGER = LoggerFactory.getLogger(OneTimeEventHook.class);
  
  private final Map<Class<? extends Event<?>>, Double> oneTimeEventMap = new HashMap<>();
  
  public OneTimeEventHook(final Class<? extends Event<?>> eventClassWithStandardConstructor, final Double probability) {
    add(eventClassWithStandardConstructor, probability);
  }
  
  public OneTimeEventHook add(final Class<? extends Event<?>> eventClassWithStandardConstructor,
      final double probability) {
    return add(eventClassWithStandardConstructor, Double.valueOf(probability));
  }
  
  public OneTimeEventHook add(final Class<? extends Event<?>> eventClassWithStandardConstructor,
      final Double probability) {
    this.oneTimeEventMap.put(eventClassWithStandardConstructor, probability);
    return this;
  }
  
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, Event<?> hookedEvent) {
    final Map<Event<?>, Double> events = new HashMap<>();
    
    for (final Entry<Class<? extends Event<?>>, Double> entry : this.oneTimeEventMap.entrySet()) {
      final Class<? extends Event<?>> eventClassWithStandardConstructor = entry.getKey();
      if (State.getEventCount(eventClassWithStandardConstructor) == 0) {
        Event<?> newEventInstance;
        try {
          newEventInstance = eventClassWithStandardConstructor.getConstructor().newInstance();
          events.put(newEventInstance, entry.getValue());
        } catch (final IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException
            | InvocationTargetException | NoSuchMethodException e) {
          LOGGER.error("Unable to create one-time event by reflection via standard constructor", e);
        }
      }
    }
    
    return events;
  }
  
}
