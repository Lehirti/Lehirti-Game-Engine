package org.atrun.intro.state;

import org.lehirti.engine.state.State;
import org.lehirti.engine.state.StringState;

public class Player extends State {
  private static final long serialVersionUID = 1L;
  
  public static enum Str implements StringState {
    NAME;
  }
}
