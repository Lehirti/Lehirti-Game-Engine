package org.atrun.locations;

import org.atrun.images.Background;
import org.atrun.intro.events.Intro01;
import org.lehirti.engine.Main;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.res.images.ImageKey;

public class DrostalToVogard extends Location {
  
  @Override
  protected ImageKey getBackgroundImageToDisplay() {
    return Background.DROSTAL_TO_VOGARD;
  }
  
  @Override
  protected void scheduleNullEvent() {
    Main.currentEvent = new Intro01();
  }
}
