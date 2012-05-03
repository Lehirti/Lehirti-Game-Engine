package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.Background;

public class Home1 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN,
    SLEEP;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Background.VILLAGE_HOME_INSIDE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    if (is(Bool.TIFFANIA_HAS_TOLD_YOU_ALL_SHE_KNOWS)) {
      addOption(Text.SLEEP, new Night3());
    } else {
      addOption(Text.SLEEP, new Night2());
    }
  }
}
