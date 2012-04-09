package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.Dita;

public class DitaIsAroused extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN,
    RUN_AFTER_HER
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    setImage(Dita.IS_AROUSED);
    
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
