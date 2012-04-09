package org.lehirti.mindcraft.intro;

import org.lehirti.engine.Main;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.mindcraft.images.Background;

public class Bathhouse extends Location<NullState> {
  
  @Override
  protected ImageKey getBackgroundImageToDisplay() {
    return Background.VILLAGE_BATHHOUSE;
  }
  
  @Override
  protected void scheduleNullEvent() {
    Main.currentEvent = new HomeVillage();
  }
}
