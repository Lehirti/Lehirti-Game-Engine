package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.TiffaniaWestwood;

public class Night1 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DREAM1
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DREAM1);
    setImage(TiffaniaWestwood.NIGHT_01);
    
    addOption(CommonText.OPTION_NEXT, new Morning1());
  }
}
