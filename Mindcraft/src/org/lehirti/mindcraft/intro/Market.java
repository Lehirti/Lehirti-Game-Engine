package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.mindcraft.images.Background;

public class Market extends Location<NullState> {
  private static final long serialVersionUID = 1L;
  
  @Override
  protected ImageKey getBackgroundImageToDisplay() {
    return Background.VILLAGE_MARKET;
  }
  
  @Override
  protected Event<?> getNullEvent() {
    return new HomeVillage();
  }
}
