package org.lehirti.luckysurvivor.cliffwest;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;

/**
 * locations by themselves are not very complex, just define a background image to display and a default event (this
 * event will be shown if there are no location hooks for this location or the location hooks have not returned any
 * events)
 */
public final class MapToCliffWest extends Location<NullState> {
  private static final long serialVersionUID = 1L;
  
  @Override
  protected ImageKey getBackgroundImageToDisplay() {
    return CliffWest.CLIFF_WEST;
  }
  
  /**
   * right now, we only have one location hook for this location and it will only return the "MeetJordanForTheFirstTime"
   * one-time event. all other times the player comes here, he will get the default event
   */
  @Override
  protected Event<?> getDefaultEvent() {
    return new CliffWestEntry();
  }
}
