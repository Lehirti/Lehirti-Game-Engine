package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.IntState;
import org.lehirti.mindcraft.images.Background;
import org.lehirti.mindcraft.images.Healer;

public class HealerHut extends EventNode {
  public static enum Text implements TextKey {
    FIRST_VISIT,
    TELL_ABOUT_ELF,
    ONLY_MENTION_PHYSICAL_SYMTOMS,
    VISIT_HEALER
    
  }
  
  public static enum Int implements IntState {
    PROGRESS;
    
    @Override
    public Long defaultValue() {
      return Long.valueOf(0);
    }
  }
  
  @Override
  protected void doEvent() {
    setBackgroundImage(Background.VILLAGE_HEALER);
    switch ((int) get(Int.PROGRESS)) {
    case 0:
      firstVisit();
      break;
    }
  }
  
  private void firstVisit() {
    setImage(Healer.SITTING_IN_HUT);
    setText(Text.FIRST_VISIT);
    set(Int.PROGRESS, 1);
    addOption(Text.ONLY_MENTION_PHYSICAL_SYMTOMS, new HealerTellSymtomsOnly());
    addOption(Text.TELL_ABOUT_ELF, new HealerTellAboutElf());
  }
}
