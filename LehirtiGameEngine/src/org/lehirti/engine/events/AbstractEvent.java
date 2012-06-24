package org.lehirti.engine.events;

import java.lang.reflect.InvocationTargetException;
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
import org.lehirti.engine.events.hooks.EventHook;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.state.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEvent<STATE extends Enum<?>> implements Event<STATE> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEvent.class);
  
  private static final Map<Class<? extends Event<?>>, Set<EventHook>> EVENT_DISPATCHERS = new HashMap<Class<? extends Event<?>>, Set<EventHook>>();
  
  public static synchronized void registerHook(final Class<? extends Event<?>> event,
      final EventHook eventHook) {
    Set<EventHook> dispatchers = EVENT_DISPATCHERS.get(event);
    if (dispatchers == null) {
      dispatchers = new HashSet<EventHook>();
      EVENT_DISPATCHERS.put(event, dispatchers);
    }
    dispatchers.add(eventHook);
  }
  
  private boolean repaintNeeded = false;
  
  protected void setImage(final ImageKey key) {
    Main.IMAGE_AREA.setImage(key);
    this.repaintNeeded = true;
  }
  
  protected void addImage(final ImageKey key) {
    Main.IMAGE_AREA.addImage(key);
    this.repaintNeeded = true;
  }
  
  protected void setBackgroundImage(final ImageKey key) {
    Main.IMAGE_AREA.setBackgroundImage(key);
    this.repaintNeeded = true;
  }
  
  protected void removeImage(final ImageKey imageKey) {
    Main.IMAGE_AREA.removeImage(imageKey);
    this.repaintNeeded = true;
  }
  
  protected void repaintImagesIfNeeded() {
    if (this.repaintNeeded) {
      try {
        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
          public void run() {
            Main.IMAGE_AREA.repaint();
          }
        });
      } catch (final InterruptedException e) {
        LOGGER.error("Thread " + Thread.currentThread().toString() + " has been interrupted; terminating thread", e);
        throw new ThreadDeath();
      } catch (final InvocationTargetException e) {
        LOGGER.error("InvocationTargetException trying to repaint image area", e);
      }
    }
  }
  
  @Override
  public boolean handleKeyEvent(final Key key) {
    return false;
  }
  
  @Override
  public boolean isLoadSavePoint() {
    return false;
  }
  
  @Override
  public void resumeFromSavePoint() {
    throw new UnsupportedOperationException(
        "Event "
            + this.getClass().getName()
            + " has no save point from which it could resume. It should never have been saved. This is a program bug. We apologize for the inconvenience.");
  }
  
  @Override
  public void newEventHasBeenLoaded() {
    throw new UnsupportedOperationException(
        "Event "
            + this.getClass().getName()
            + " is not in a state from which it can resume a newly loaded game. This is a program bug. We apologize for the inconvenience.");
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public STATE getEventState() {
    return (STATE) State.get(getClass());
  }
  
  @Override
  public void setEventState(final STATE newState) {
    State.set(getClass(), newState);
  }
  
  @Override
  public Event<?> getActualEvent(Event<?> previousEvent) {
    final Set<EventHook> dispatchersForThisLocation = EVENT_DISPATCHERS.get(this.getClass());
    
    final Map<Event<?>, Double> probablityPerEventMap = new HashMap<Event<?>, Double>();
    if (dispatchersForThisLocation != null) {
      for (final EventHook dispatcher : dispatchersForThisLocation) {
        probablityPerEventMap.putAll(dispatcher.getCurrentEvents(previousEvent));
      }
    }
    if (probablityPerEventMap.isEmpty()) {
      return this;
    } else {
      final List<Event<?>> probabilityAlwaysEvents = getProbabilityAlwaysEvents(probablityPerEventMap);
      if (!probabilityAlwaysEvents.isEmpty()) {
        return getRandomProbabilityAlwaysEvent(probabilityAlwaysEvents);
      } else {
        final Map<Event<?>, Double> eventsToChooseFrom = removeRegularEvents(probablityPerEventMap);
        final double totalProbabilityOfRegularEvents = getTotalProbablility(eventsToChooseFrom.values());
        if (totalProbabilityOfRegularEvents < 100.0D) {
          eventsToChooseFrom.putAll(getDefaultEvents(probablityPerEventMap.keySet(),
              100.0D - totalProbabilityOfRegularEvents));
        } else if (totalProbabilityOfRegularEvents > 100.0D) {
          scaleTotalProbabilityTo100Percent(eventsToChooseFrom, totalProbabilityOfRegularEvents);
        }
        return getRandomRegularOrDefaultEvent(eventsToChooseFrom);
      }
    }
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
      if (eventEntry.getValue().doubleValue() > EventHook.PROBABILITY_DEFAULT + 0.5 /* rounding errors */) {
        regularEvents.put(eventEntry.getKey(), eventEntry.getValue());
        itr.remove();
      }
    }
    return regularEvents;
  }
  
  private Event<?> getRandomRegularOrDefaultEvent(final Map<Event<?>, Double> eventsToChooseFrom) {
    double remainingProbabilityFromDieRoll = State.DIE.nextDouble() * 100.0D;
    for (final Map.Entry<Event<?>, Double> entry : eventsToChooseFrom.entrySet()) {
      if (remainingProbabilityFromDieRoll < entry.getValue().doubleValue()) {
        return entry.getKey();
      }
      remainingProbabilityFromDieRoll -= entry.getValue().doubleValue();
    }
    return this;
  }
  
  private static Event<?> getRandomProbabilityAlwaysEvent(final List<Event<?>> probabilityAlwaysEvents) {
    if (probabilityAlwaysEvents.size() > 1) {
      LOGGER.warn(probabilityAlwaysEvents.size() + " events with probabilty \"ALWAYS\". Only one will occur.");
    }
    return probabilityAlwaysEvents.get(State.DIE.nextInt(probabilityAlwaysEvents.size()));
  }
  
  private static List<Event<?>> getProbabilityAlwaysEvents(final Map<Event<?>, Double> probablityPerEventMap) {
    final List<Event<?>> probAlwaysEvents = new LinkedList<Event<?>>();
    for (final Map.Entry<Event<?>, Double> entry : probablityPerEventMap.entrySet()) {
      if (entry.getValue().doubleValue() < EventHook.PROBABILITY_ALWAYS + 0.5 /* rounding errors */) {
        probAlwaysEvents.add(entry.getKey());
      }
    }
    return probAlwaysEvents;
  }
}
