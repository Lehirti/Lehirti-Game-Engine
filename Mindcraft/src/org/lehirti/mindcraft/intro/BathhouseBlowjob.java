package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;

public class BathhouseBlowjob extends EventNode<NullState> {
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(Intro.BLOWJOB_IN_BATHHOUSE);
  }
  
  @Override
  protected void doEvent() {
    setText(Intro.BLOWJOB_IN_BATHHOUSE);
    set(Bool.GOT_BLOWJOB_IN_BATHHOUSE, true);
    set(Bool.YOU_ARE_HORNY, false);
    
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
