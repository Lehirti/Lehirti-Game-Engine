package org.lehirti.mindcraft.intro;

import org.lehirti.engine.state.BoolState;

public enum Bool implements BoolState {
  HAS_MASTURBATED_IN_THE_MORNING,
  GOT_BLOWJOB_IN_BATHHOUSE;
  
  @Override
  public Boolean defaultValue() {
    return false;
  }
}
