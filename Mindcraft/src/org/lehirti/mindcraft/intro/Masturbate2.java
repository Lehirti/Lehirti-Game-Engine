package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class Masturbate2 extends EventNode {
  public static enum Text implements TextKey {
    TEXT
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TEXT);
    setImage(Intro.MASTURBATE);
    set(Bool.YOU_ARE_HORNY, false);
    
    addOption(CommonText.LEAVE_HOUSE, new HomeVillage());
  }
}
