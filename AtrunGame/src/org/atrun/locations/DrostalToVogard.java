package org.atrun.locations;

import org.atrun.images.Background;
import org.atrun.intro.events.Intro01;
import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Location;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;

public class DrostalToVogard extends Location<NullState> {
  
  @Override
  protected ImageKey getBackgroundImageToDisplay() {
    return Background.DROSTAL_TO_VOGARD;
  }
  
  @Override
  protected Event<?> getNullEvent() {
    return new Intro01();
  }
}
