package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.TextKey;

public class Masturbate extends EventNode {
  public static enum Text implements TextKey {
    TEXT,
    OPTION_LEAVE_HOUSE
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TEXT);
    setImage(Intro.MASTURBATE);
    
    addOption(Text.OPTION_LEAVE_HOUSE, new HomeVillage());
  }
}
