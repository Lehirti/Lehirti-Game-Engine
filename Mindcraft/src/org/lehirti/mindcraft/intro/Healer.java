package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.IntState;
import org.lehirti.mindcraft.images.Background;

public class Healer extends EventNode {
  public static enum Text implements TextKey {
    ELF,
    OPTION_LEAVE
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
    setImage(null);
    setText(null);
    switch ((int) get(Int.PROGRESS)) {
    case 0:
      firstVisit();
      return;
    }
  }
  
  private void firstVisit() {
    addOption(Text.OPTION_LEAVE, new HomeVillage());
    
  }
}
