package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.TextKey;

public class Masturbate extends EventNode {
  public static enum Text implements TextKey {
    TEXT
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TEXT);
    setImage(Intro.MASTURBATE);
    set(Bool.HAS_MASTURBATED_IN_THE_MORNING, true);
    
    addOption(HealerHut.Text.VISIT_HEALER, new HealerHut());
  }
}
