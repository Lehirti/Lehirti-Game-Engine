package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

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
