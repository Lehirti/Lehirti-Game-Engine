package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.text.TextKey;
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
  protected void doEvent() {
    setBackgroundImage(Background.VILLAGE_HEALER);
    setImage(Healer.SITTING_IN_HUT);
    setText(Text.FIRST_VISIT);
    addOption(Text.ONLY_MENTION_PHYSICAL_SYMTOMS, new HealerTellSymtomsOnly());
    addOption(Text.TELL_ABOUT_ELF, new HealerTellAboutElf());
  }
}
