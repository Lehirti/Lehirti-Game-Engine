package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class BuryTheDead extends EventNode<NullState> {
  public static enum Text implements TextKey {
    BURY_WITH_SHOVEL,
    BURY_WITHOUT_SHOVEL;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.nullChange();
  }
  
  @Override
  protected void doEvent() {
    if (is(Bool.HAS_SHOVEL)) {
      setText(Text.BURY_WITH_SHOVEL);
    } else {
      setText(Text.BURY_WITHOUT_SHOVEL);
    }
    
    addOption(Key.OPTION_EAST, CommonText.OPTION_NEXT, new Outside2());
  }
}
