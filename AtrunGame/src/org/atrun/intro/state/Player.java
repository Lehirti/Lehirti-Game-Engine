package org.atrun.intro.state;

import org.lehirti.state.StateObject;
import org.lehirti.state.StringState;

public class Player extends StateObject {
  private static final long serialVersionUID = 1L;
  
  public static enum Str implements StringState {
    NAME("");
    
    private final String defaultValue;
    
    private Str(final String defaultValue) {
      this.defaultValue = defaultValue;
    }
    
    @Override
    public String defaultValue() {
      return this.defaultValue;
    }
  }
}
