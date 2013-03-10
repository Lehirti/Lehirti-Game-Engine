package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;

public class Masturbate2 extends EventNode<NullState> {
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(Intro.MASTURBATE);
  }
  
  @Override
  protected void doEvent() {
    setText(Intro.MASTURBATE);
    set(Bool.YOU_ARE_HORNY, false);
    
    addOption(CommonText.OPTION_LEAVE_HOUSE, new HomeVillage());
  }
}
