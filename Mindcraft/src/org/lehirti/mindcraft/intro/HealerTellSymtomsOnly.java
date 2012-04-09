package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class HealerTellSymtomsOnly extends EventNode {
  public static enum Text implements TextKey {
    TELL_SYMTOMS_ONLY,
    HORNY
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TELL_SYMTOMS_ONLY);
    if (is(Bool.YOU_ARE_HORNY)) {
      addText(Text.HORNY);
      set(Bool.HEALER_KNOWS_ABOUT_ELF, true);
      addOption(CommonText.OPTION_LEAVE, new Home1());
    } else {
      addOption(CommonText.OPTION_LEAVE, new HomeVillage());
    }
  }
}
