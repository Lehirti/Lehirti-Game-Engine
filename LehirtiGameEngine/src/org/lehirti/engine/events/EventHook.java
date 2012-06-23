package org.lehirti.engine.events;

import java.util.Map;

import org.lehirti.engine.state.StaticInitializer;

public interface EventHook extends StaticInitializer {
  /**
   * should only be used for one-off events since an event with probability PROBABILITY_ALWAYS will prevent regular
   * events from occurring
   */
  public static final Double PROBABILITY_ALWAYS = Double.valueOf(-100.0);
  
  /**
   * should be used for "filler" events.
   */
  public static final Double PROBABILITY_DEFAULT = Double.valueOf(-1.0);
  
  /**
   * @param previousEvent
   *          the event from which the player is coming to the hooked event
   * @return all events that may happen right now at this location with the probability (in percent) of them happening.<br/>
   *         events with probability PROBABILITY_ALWAYS and PROBABILITY_DEFAULT are special.<br/>
   *         if events with probability PROBABILITY_ALWAYS are present, one of them is chosen randomly and regular
   *         events are ignored.<br/>
   *         if the sum of the probabilities of all non-special events is less than 100, the remaining probability is
   *         allocated to events with probability PROBABILITY_DEFAULT. if there is no such event, the remaining
   *         probability remains unused (chance of "nothing" happening).<br/>
   *         if the sum of the probabilities of all non-special events is greater than 100, all probabilities are scaled
   *         back proportionately.
   */
  public abstract Map<Event<?>, Double> getCurrentEvents(Event<?> previousEvent);
}
