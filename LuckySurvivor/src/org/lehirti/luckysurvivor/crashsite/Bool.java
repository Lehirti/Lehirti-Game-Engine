package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.state.BoolState;

public enum Bool implements BoolState {
  SHELTER_HAS_BEEN_BUILT,
  HAS_SHOVEL,
  HAS_METAL_STRUT,
  HAS_LONG_METAL_POLES,
  HAS_SHEETS_OF_FABRIC,
  HAS_STRECHER,
  HAS_PEANUTS_AND_BOTTLED_WATER;
  
  @Override
  public Boolean defaultValue() {
    return Boolean.FALSE;
  }
}
