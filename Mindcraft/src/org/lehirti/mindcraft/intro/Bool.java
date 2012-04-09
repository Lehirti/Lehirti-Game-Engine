package org.lehirti.mindcraft.intro;

import org.lehirti.engine.state.BoolState;

public enum Bool implements BoolState {
  YOU_ARE_HORNY,
  GOT_BLOWJOB_IN_BATHHOUSE,
  MARKET_SELLER_FUCKED,
  HEALER_KNOWS_ABOUT_ELF;
  
  @Override
  public Boolean defaultValue() {
    return Boolean.FALSE;
  }
}
