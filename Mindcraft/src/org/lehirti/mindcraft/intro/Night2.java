package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.TiffaniaWestwood;

public class Night2 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DREAM2
  }
  
  @Override
  protected void doEvent() {
    setBackgroundImage(null);
    setText(Text.DREAM2);
    setImage(TiffaniaWestwood.NIGHT_02);
    
    addOption(CommonText.OPTION_NEXT, new Morning2());
  }
}
