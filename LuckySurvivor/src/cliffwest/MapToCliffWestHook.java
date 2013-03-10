package cliffwest;

import java.util.HashMap;
import java.util.Map;

import lge.events.AbstractEvent;
import lge.events.Event;
import lge.events.hooks.EventHook;
import lge.state.DateTime;
import lge.state.State;

import npc.emily.MeetEmily;

public final class MapToCliffWestHook implements EventHook {
  static {
    /**
     * EventHooks get registered with Events like this; we tell the game that when the player enters the event
     * "MapToCliffWest" it should ask the event hook "MeetJordanForTheFirstTimeHook" - this class - if there are events
     * available for the player to encounter; there may be multiple event hooks for one event.
     */
    AbstractEvent.registerHook(MapToCliffWest.class, new MapToCliffWestHook());
  }
  
  /**
   * now, whenever the player enters the MapToCliffWest event, the getCurrentEvents(Event<?> previousEvent) method is
   * called; it must return all available events, together with the probability of the event happening
   */
  @Override
  public Map<Event<?>, Double> getCurrentEvents(final Event<?> previousEvent, Event<?> hookedEvent) {
    // we start out with an empty map, meaning no events from this hook
    final Map<Event<?>, Double> events = new HashMap<>();
    
    if (State.getEventCount(MapToCliffWest.class) > 0 // player has been here before
        && DateTime.gethhmmss() > 30000 // AND the necessary amount of time has passed
        && State.getEventCount(MeetJordanForTheFirstTime.class) == 0) { // AND he has not met jordan, yet
      // the condition for meeting jordan for the first time is met, so we add the event
      events.put(new MeetJordanForTheFirstTime(), Double.valueOf(100.0));
    }
    
    if (State.getEventCount(MeetJordanForTheFirstTime.class) > 0 // player has met jordan
        && State.getEventCount(MeetEmily.class) == 0) { // AND has not met, emily, yet
      // the condition for meeting jordan for the first time is met, so we add the event
      events.put(new MeetEmily(), Double.valueOf(50.0));
    }
    return events;
  }
}
