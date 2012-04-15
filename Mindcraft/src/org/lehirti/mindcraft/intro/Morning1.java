package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.Background;

public class Morning1 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    EXPLANATION,
    OPTION_MASTURBATE
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Background.VILLAGE_HOME_INSIDE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.EXPLANATION);
    set(Bool.YOU_ARE_HORNY, true);
    
    addOption(Text.OPTION_MASTURBATE, new Masturbate());
    addOption(HealerHut.Text.VISIT_HEALER, new HealerHut());
  }
}
