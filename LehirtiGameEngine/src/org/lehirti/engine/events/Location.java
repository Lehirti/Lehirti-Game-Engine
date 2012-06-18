package org.lehirti.engine.events;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.lehirti.engine.Main;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.state.StateObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Location<STATE extends Enum<?>> extends AbstractEvent<STATE> implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Location.class);
  
  private static final Map<Class<? extends Location<?>>, Set<LocationHook>> LOCATION_DISPATCHERS = new HashMap<Class<? extends Location<?>>, Set<LocationHook>>();
  
  public static synchronized void registerDispatcher(final Class<? extends Location<?>> location,
      final LocationHook dispatcher) {
    Set<LocationHook> dispatchers = LOCATION_DISPATCHERS.get(location);
    if (dispatchers == null) {
      dispatchers = new HashSet<LocationHook>();
      LOCATION_DISPATCHERS.put(location, dispatchers);
    }
    dispatchers.add(dispatcher);
  }
  
  private final Event<?> nextEvent;
  
  public Location() {
    final Set<LocationHook> dispatchersForThisLocation = LOCATION_DISPATCHERS.get(this.getClass());
    
    final Map<Event<?>, Double> probablityPerEventMap = new HashMap<Event<?>, Double>();
    if (dispatchersForThisLocation != null) {
      for (final LocationHook dispatcher : dispatchersForThisLocation) {
        probablityPerEventMap.putAll(dispatcher.getCurrentEvents());
      }
    }
    if (probablityPerEventMap.isEmpty()) {
      this.nextEvent = getDefaultEvent();
    } else {
      final List<Event<?>> probabilityAlwaysEvents = getProbabilityAlwaysEvents(probablityPerEventMap);
      if (!probabilityAlwaysEvents.isEmpty()) {
        this.nextEvent = getRandomProbabilityAlwaysEvent(probabilityAlwaysEvents);
      } else {
        final Map<Event<?>, Double> eventsToChooseFrom = removeRegularEvents(probablityPerEventMap);
        final double totalProbabilityOfRegularEvents = getTotalProbablility(eventsToChooseFrom.values());
        if (totalProbabilityOfRegularEvents < 100.0D) {
          eventsToChooseFrom.putAll(getDefaultEvents(probablityPerEventMap.keySet(),
              100.0D - totalProbabilityOfRegularEvents));
        } else if (totalProbabilityOfRegularEvents > 100.0D) {
          scaleTotalProbabilityTo100Percent(eventsToChooseFrom, totalProbabilityOfRegularEvents);
        }
        this.nextEvent = getRandomRegularOrDefaultEvent(eventsToChooseFrom);
      }
    }
  }
  
  @Override
  public Collection<ImageKey> getAllUsedImages() {
    final Set<ImageKey> usedImages = new HashSet<ImageKey>();
    usedImages.add(getBackgroundImageToDisplay());
    usedImages.addAll(this.nextEvent.getAllUsedImages());
    return usedImages;
  }
  
  @Override
  public void execute() {
    LOGGER.info("Location: {}", getClass().getName());
    
    setBackgroundImage(getBackgroundImageToDisplay());
    repaintImagesIfNeeded();
    
    Main.currentEvent = this.nextEvent;
  }
  
  /**
   * the total probability of all events must be totalProbabilityOfEvents (!= 0) before this method call and will be 100
   * after this method call
   * 
   * @param events
   * @param totalProbabilityOfEvents
   */
  private static void scaleTotalProbabilityTo100Percent(final Map<Event<?>, Double> events,
      final double totalProbabilityOfEvents) {
    final double scalingFactor = 100.0D / totalProbabilityOfEvents;
    for (final Map.Entry<Event<?>, Double> entry : events.entrySet()) {
      entry.setValue(Double.valueOf(entry.getValue().doubleValue() * scalingFactor));
    }
  }
  
  private static Map<Event<?>, Double> getDefaultEvents(final Set<Event<?>> defaultEvents,
      final double remainingProbability) {
    if (defaultEvents.isEmpty()) {
      return Collections.emptyMap();
    }
    final Double probabilityPerDefaultEvent = Double.valueOf(remainingProbability / defaultEvents.size());
    final Map<Event<?>, Double> defaultEventsMap = new HashMap<Event<?>, Double>();
    for (final Event<?> defaultEvent : defaultEvents) {
      defaultEventsMap.put(defaultEvent, probabilityPerDefaultEvent);
    }
    return defaultEventsMap;
  }
  
  private static double getTotalProbablility(final Collection<Double> probabilities) {
    double total = 0.0D;
    for (final Double prob : probabilities) {
      total += prob;
    }
    return total;
  }
  
  /**
   * @param probablityPerEventMap
   * @return regular events
   */
  private static Map<Event<?>, Double> removeRegularEvents(final Map<Event<?>, Double> probablityPerEventMap) {
    final Map<Event<?>, Double> regularEvents = new HashMap<Event<?>, Double>();
    final Iterator<Map.Entry<Event<?>, Double>> itr = probablityPerEventMap.entrySet().iterator();
    while (itr.hasNext()) {
      final Entry<Event<?>, Double> eventEntry = itr.next();
      if (eventEntry.getValue().doubleValue() > LocationHook.PROBABILITY_DEFAULT + 0.5 /* rounding errors */) {
        regularEvents.put(eventEntry.getKey(), eventEntry.getValue());
        itr.remove();
      }
    }
    return regularEvents;
  }
  
  private Event<?> getRandomRegularOrDefaultEvent(final Map<Event<?>, Double> eventsToChooseFrom) {
    double remainingProbabilityFromDieRoll = StateObject.DIE.nextDouble() * 100.0D;
    for (final Map.Entry<Event<?>, Double> entry : eventsToChooseFrom.entrySet()) {
      if (remainingProbabilityFromDieRoll < entry.getValue().doubleValue()) {
        return entry.getKey();
      }
      remainingProbabilityFromDieRoll -= entry.getValue().doubleValue();
    }
    return getDefaultEvent();
  }
  
  private static Event<?> getRandomProbabilityAlwaysEvent(final List<Event<?>> probabilityAlwaysEvents) {
    if (probabilityAlwaysEvents.size() > 1) {
      LOGGER.warn(probabilityAlwaysEvents.size() + " events with probabilty \"ALWAYS\". Only one will occur.");
    }
    return probabilityAlwaysEvents.get(StateObject.DIE.nextInt(probabilityAlwaysEvents.size()));
  }
  
  private static List<Event<?>> getProbabilityAlwaysEvents(final Map<Event<?>, Double> probablityPerEventMap) {
    final List<Event<?>> probAlwaysEvents = new LinkedList<Event<?>>();
    for (final Map.Entry<Event<?>, Double> entry : probablityPerEventMap.entrySet()) {
      if (entry.getValue().doubleValue() < LocationHook.PROBABILITY_ALWAYS + 0.5 /* rounding errors */) {
        probAlwaysEvents.add(entry.getKey());
      }
    }
    return probAlwaysEvents;
  }
  
  protected abstract Event<?> getDefaultEvent();
  
  protected abstract ImageKey getBackgroundImageToDisplay();
}
