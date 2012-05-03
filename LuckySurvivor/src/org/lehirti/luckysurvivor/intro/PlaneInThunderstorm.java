package org.lehirti.luckysurvivor.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class PlaneInThunderstorm extends EventNode<NullState> {
  public static enum Text implements TextKey {
    PLANE_IN_THUNDERSTORM,
    OPTION_SECURE_OWN_MASK;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(IntroImage.PLANE_IN_THUNDERSTORM);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.PLANE_IN_THUNDERSTORM);
    
    addOption(Text.OPTION_SECURE_OWN_MASK, new TheCrash());
  }
}
