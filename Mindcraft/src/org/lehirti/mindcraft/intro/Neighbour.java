package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.mindcraft.images.Background;

public class Neighbour extends Location<NullState> {
  
  @Override
  protected ImageKey getBackgroundImageToDisplay() {
    return Background.VILLAGE_NEIGHBOUR;
  }
  
  @Override
  protected Event<?> getNullEvent() {
    return new HomeVillage();
  }
}