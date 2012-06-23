package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.gui.Key;

public class SearchFuselageForSomethingUsefull extends StandardEvent {
  private static final long serialVersionUID = 1L;
  
  public SearchFuselageForSomethingUsefull() {
    super(Key.OPTION_LEAVE, CrashSite.INSIDE_FUSELAGE_NON_BURNING, Plane2_Fuselage.Text.NOTHING_OF_VALUE_WAS_FOUND,
        new Plane2_Fuselage());
  }
}
