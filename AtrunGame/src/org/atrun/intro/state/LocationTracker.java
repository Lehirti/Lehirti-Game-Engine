package org.atrun.intro.state;

import org.lehirti.engine.state.IntState;
import org.lehirti.engine.state.State;

public class LocationTracker extends State {
  private static final long serialVersionUID = 1L;
  
  public static enum Int implements IntState {
    BEEN_TO_INTRO_04;
  }
}
