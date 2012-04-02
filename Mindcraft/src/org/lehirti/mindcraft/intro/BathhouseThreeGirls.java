package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class BathhouseThreeGirls extends EventNode {
  public static enum Text implements TextKey {
    MAIN;
  }
  
  @Override
  protected void doEvent() {
    setImage(Intro.THREE_GIRLS_IN_BATHHOUSE);
    setText(Text.MAIN);
    
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
