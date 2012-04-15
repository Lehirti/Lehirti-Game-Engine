package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;

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
