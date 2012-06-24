package org.lehirti.luckysurvivor.cliffwest;

import java.util.HashMap;
import java.util.Map;

import org.lehirti.engine.events.AbstractEvent;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.hooks.EventHook;
import org.lehirti.engine.state.DateTime;
import org.lehirti.engine.state.State;

public final class MeetJordanForTheFirstTimeHook implements EventHook {
  static {
    /**
     * EventHooks get registered with Events like this; we tell the game that when the player enters the event
     * "MapToCliffWest" it should ask the event hook "MeetJordanForTheFirstTimeHook" - this class - if there are events
     * available for the player to encounter; there may be multiple event hooks for one event.
     */
    AbstractEvent.registerHook(MapToCliffWest.class, new MeetJordanForTheFirstTimeHook());
  }
  
  /**
   * now, whenever the player enters the MapToCliffWest event, the getCurrentEvents(Event<?> previousEvent) method is
   * called; it must return all available events, together with the probability of the event happening
   */
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent) {
    // we start out with an empty map, meaning no events from this hook
    final Map<Event<?>, Double> events = new HashMap<Event<?>, Double>();
    
    /*
     * TODO since in-game time is not really progressing, yet, i just check if the player has been here before, has not
     * met jordan, yet and if the time is greater than 0 (which is always true); once time is progressing properly, this
     * can be changed easily. if you want to show the event only after day 103; 18 hours 44 minutes and 3 seconds just
     * write DateTime.getDDhhmmss() > 103184403
     */
    if (State.getEventCount(MapToCliffWest.class) > 0 // player has been here before
        && DateTime.getDDhhmmss() > 0 // AND the necessary amount of time has passed
        && State.getEventCount(MeetJordanForTheFirstTime.class) == 0) { // AND he has not met jordan, yet
      // the condition for meeting jordan for the first time is met, so we add the event
      // TODO i took the liberty of making the event only happening with a 25% probability to make up for the time thing
      events.put(new MeetJordanForTheFirstTime(), Double.valueOf(25.0));
    }
    return events;
  }
}
