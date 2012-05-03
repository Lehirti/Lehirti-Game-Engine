package org.lehirti.luckysurvivor.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class BoardPlane extends EventNode<NullState> {
  public static enum Text implements TextKey {
    PLANE_DESCRIPTION,
    OPTION_TRY_SLEEP,
    TRY_SLEEP,
    OPTION_START_CONVERSATION,
    START_CONVERSATION,
    OPTION_ASK_FOR_OTHER_SEAT,
    ASK_FOR_OTHER_SEAT;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(IntroImage.INSIDE_PLANE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.PLANE_DESCRIPTION);
    
    addOption(Text.OPTION_TRY_SLEEP, new TextOnlyEvent(Text.TRY_SLEEP, new PlaneInThunderstorm()));
    addOption(Text.OPTION_START_CONVERSATION, new TextOnlyEvent(Text.START_CONVERSATION, new PlaneInThunderstorm()));
    addOption(Text.OPTION_ASK_FOR_OTHER_SEAT, new TextOnlyEvent(Text.ASK_FOR_OTHER_SEAT, new PlaneInThunderstorm()));
  }
}
