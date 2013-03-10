package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;

public class FuckMarketSeller extends EventNode<NullState> {
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(Intro.MARKET_SELLER);
  }
  
  @Override
  protected void doEvent() {
    setText(Intro.MARKET_SELLER);
    set(Bool.YOU_ARE_HORNY, false);
    set(Bool.MARKET_SELLER_FUCKED, true);
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
