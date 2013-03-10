package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

public class HealerTellAboutElf extends EventNode<NullState> {
  public static enum Text implements TextKey {
    TELL_ABOUT_ELF
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TELL_ABOUT_ELF);
    set(Bool.HEALER_KNOWS_ABOUT_ELF, true);
    
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
