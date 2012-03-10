package org.atrun.locations;

import org.atrun.backgrounds.Background;
import org.atrun.intro.events.Intro01;
import org.lehirti.Main;
import org.lehirti.events.Location;
import org.lehirti.res.images.ImageKey;

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
