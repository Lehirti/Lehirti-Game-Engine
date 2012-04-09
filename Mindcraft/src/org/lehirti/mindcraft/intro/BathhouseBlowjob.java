package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.text.CommonText;

public class BathhouseBlowjob extends EventNode<NullState> {
  @Override
  protected void doEvent() {
    setImage(Intro.BLOWJOB_IN_BATHHOUSE);
    setText(Intro.BLOWJOB_IN_BATHHOUSE);
    set(Bool.GOT_BLOWJOB_IN_BATHHOUSE, true);
    set(Bool.YOU_ARE_HORNY, false);
    
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
