package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

public class HealerTellSymtomsOnly extends EventNode<NullState> {
  public static enum Text implements TextKey {
    TELL_SYMTOMS_ONLY,
    HORNY
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
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
