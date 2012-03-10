package org.atrun.intro.state;

import org.lehirti.state.IntState;
import org.lehirti.state.StateObject;

public class LocationTracker extends StateObject {
  private static final long serialVersionUID = 1L;
  
  public static enum Int implements IntState {
    BEEN_TO_INTRO_04(0);
    
    private final int defaultValue;
    
    private Int(final int defaultValue) {
      this.defaultValue = Integer.valueOf(defaultValue);
    }
    
    @Override
    public Integer defaultValue() {
      return this.defaultValue;
    }
  }
}
