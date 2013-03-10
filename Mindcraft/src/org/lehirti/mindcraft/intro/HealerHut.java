package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

import org.lehirti.mindcraft.images.Background;
import org.lehirti.mindcraft.images.Healer;

public class HealerHut extends EventNode<NullState> {
  public static enum Text implements TextKey {
    FIRST_VISIT,
    TELL_ABOUT_ELF,
    ONLY_MENTION_PHYSICAL_SYMTOMS,
    VISIT_HEALER
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Background.VILLAGE_HEALER, Healer.SITTING_IN_HUT);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.FIRST_VISIT);
    addOption(Text.ONLY_MENTION_PHYSICAL_SYMTOMS, new HealerTellSymtomsOnly());
    addOption(Text.TELL_ABOUT_ELF, new HealerTellAboutElf());
  }
}
