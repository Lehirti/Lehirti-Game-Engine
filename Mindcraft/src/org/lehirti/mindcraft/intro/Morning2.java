package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.Background;

public class Morning2 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MORNING2,
    OPTION_MASTURBATE,
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MORNING2);
    setBackgroundImage(Background.VILLAGE_HOME_INSIDE);
    setImage(null);
    set(Bool.YOU_ARE_HORNY, true);
    
    addOption(Text.OPTION_MASTURBATE, new Masturbate2());
    addOption(CommonText.LEAVE_HOUSE, new HomeVillage());
  }
}
