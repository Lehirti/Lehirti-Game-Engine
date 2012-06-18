package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;

public final class MapToCrashSite extends Location<NullState> {
  private static final long serialVersionUID = 1L;
  
  @Override
  protected ImageKey getBackgroundImageToDisplay() {
    return CrashSite.OUTSIDE_PLANE_NON_BURNING;
  }
  
  @Override
  protected Event<?> getDefaultEvent() {
    return new Outside2();
  }
}
