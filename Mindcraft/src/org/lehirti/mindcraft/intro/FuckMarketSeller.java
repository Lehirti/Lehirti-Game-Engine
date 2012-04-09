package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.text.CommonText;

public class FuckMarketSeller extends EventNode<NullState> {
  
  @Override
  protected void doEvent() {
    setImage(Intro.MARKET_SELLER);
    setText(Intro.MARKET_SELLER);
    set(Bool.YOU_ARE_HORNY, false);
    set(Bool.MARKET_SELLER_FUCKED, true);
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
