package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class HealerTellAboutElf extends EventNode {
  public static enum Text implements TextKey {
    TELL_ABOUT_ELF
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TELL_ABOUT_ELF);
    set(Bool.HEALER_KNOWS_ABOUT_ELF, Boolean.TRUE);
    
    addOption(CommonText.OPTION_NEXT, new HomeVillage());
  }
}
