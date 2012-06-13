package org.atrun.intro.state;

import org.lehirti.engine.state.StateObject;
import org.lehirti.engine.state.StringState;

public class Player extends StateObject {
  private static final long serialVersionUID = 1L;
  
  public static enum Str implements StringState {
    NAME;
  }
}
