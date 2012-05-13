package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.EventFactory;
import org.lehirti.engine.state.StateObject;

public final class CrashSiteLocationFactory implements EventFactory {
  
  @Override
  public Event<?> getInstance() {
    if (StateObject.is(Bool.SHELTER_HAS_BEEN_CREATED)) {
      return null; // TODO
    } else {
      return new CrashSiteAfterCrash();
    }
  }
}
