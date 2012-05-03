package org.lehirti.luckysurvivor.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class Bus extends EventNode<NullState> {
  public static enum Text implements TextKey {
    BUS_DESCRIPTION,
    OPTION_ENTER_PLANE;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(IntroImage.BUS);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.BUS_DESCRIPTION);
    
    addOption(Text.OPTION_ENTER_PLANE, new BoardPlane());
  }
}
