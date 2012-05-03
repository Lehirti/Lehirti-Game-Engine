package org.lehirti.mindcraft.regionhometocapitol;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.mindcraft.images.Background;
import org.lehirti.mindcraft.intro.HomeVillage;

public class HomeVillageToCapitol extends Location<NullState> {
  private static final long serialVersionUID = 1L;
  
  @Override
  protected ImageKey getBackgroundImageToDisplay() {
    return Background.VILLAGE_TO_CAPITOL;
  }
  
  @Override
  protected Event<?> getNullEvent() {
    return new HomeVillage(); // TODO
  }
}
