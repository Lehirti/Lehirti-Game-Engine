package org.lehirti.luckysurvivor.planearea;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;

public class OutsidePlane extends Location<NullState> {
  private static final long serialVersionUID = 1L;
  
  @Override
  protected ImageKey getBackgroundImageToDisplay() {
    return null; // TODO daytime-dependent image
  }
  
  @Override
  protected Event<?> getNullEvent() {
    // TODO Auto-generated method stub
    return null;
  }
}
