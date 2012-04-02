package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class BathhouseBlowjob extends EventNode {
  public static enum Text implements TextKey {
    MAIN;
  }
  
  @Override
  protected void doEvent() {
    setImage(Intro.BLOWJOB_IN_BATHHOUSE);
    setText(Text.MAIN);
    set(Bool.GOT_BLOWJOB_IN_BATHHOUSE, true);
    
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
