package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;

public class SearchFuselageForSomethingUsefull extends Location<NullState> {
  private static final long serialVersionUID = 1L;
  
  @Override
  protected ImageKey getBackgroundImageToDisplay() {
    return CrashSite.INSIDE_FUSELAGE_NON_BURNING;
  }
  
  @Override
  protected Event<?> getNullEvent() {
    return new TextOnlyEvent(Key.OPTION_LEAVE, Plane2_Fuselage.Text.NOTHING_OF_VALUE_WAS_FOUND, new Plane2_Fuselage());
  }
}
