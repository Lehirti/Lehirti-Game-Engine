package org.lehirti.luckysurvivor.map;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.EventFactory;
import org.lehirti.engine.state.StateObject;
import org.lehirti.luckysurvivor.planearea.Bool;
import org.lehirti.luckysurvivor.planearea.CrashSiteAfterCrash;

public final class Jungle2UphillLocationFactory implements EventFactory {
  
  @Override
  public Event<?> getInstance() {
    if (StateObject.is(Bool.SHELTER_HAS_BEEN_CREATED)) {
      return null; // TODO
    } else {
      return new CrashSiteAfterCrash();
    }
  }
}
