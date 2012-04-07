package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class HealerTellSymtomsOnly extends EventNode {
  public static enum Text implements TextKey {
    TELL_SYMTOMS_ONLY
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TELL_SYMTOMS_ONLY);
    
    addOption(CommonText.OPTION_NEXT, new HomeVillage());
  }
}
