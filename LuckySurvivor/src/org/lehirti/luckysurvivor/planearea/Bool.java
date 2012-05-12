package org.lehirti.luckysurvivor.planearea;

import org.lehirti.engine.state.BoolState;

public enum Bool implements BoolState {
  SHELTER_HAS_BEEN_CREATED;
  
  @Override
  public Boolean defaultValue() {
    return Boolean.FALSE;
  }
}
