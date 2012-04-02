package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.CommonText;

public class BathhouseGuysOnly extends EventNode {
  @Override
  protected void doEvent() {
    setImage(Intro.GUYS_ONLY_IN_BATHHOUSE);
    setText(Intro.GUYS_ONLY_IN_BATHHOUSE);
    
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
