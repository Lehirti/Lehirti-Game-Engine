package intro;

import lge.events.EventNode;
import lge.events.StandardEvent;
import lge.events.TextOnlyEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

public class BoardPlane extends EventNode<NullState> {
  public static enum Text implements TextKey {
    PLANE_DESCRIPTION,
    OPTION_TRY_SLEEP,
    TRY_SLEEP,
    OPTION_START_CONVERSATION,
    START_CONVERSATION,
    OPTION_ASK_FOR_OTHER_SEAT,
    ASK_FOR_OTHER_SEAT,
    OPTION_FOLLOW_STEWARDESS,
    FUCK_STEWARDESS;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(IntroImage.INSIDE_PLANE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.PLANE_DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_FOLLOW_STEWARDESS, new StandardEvent(Key.OPTION_NORTH,
        IntroImage.FUCK_STEWARDESS, Text.FUCK_STEWARDESS, new PlaneInThunderstorm()));
    addOption(Key.OPTION_WEST, Text.OPTION_TRY_SLEEP, new TextOnlyEvent(Key.OPTION_WEST, Text.TRY_SLEEP,
        new PlaneInThunderstorm()));
    addOption(Key.OPTION_SOUTH, Text.OPTION_START_CONVERSATION, new TextOnlyEvent(Key.OPTION_SOUTH,
        Text.START_CONVERSATION, new PlaneInThunderstorm()));
    addOption(Key.OPTION_EAST, Text.OPTION_ASK_FOR_OTHER_SEAT, new TextOnlyEvent(Key.OPTION_EAST,
        Text.ASK_FOR_OTHER_SEAT, new PlaneInThunderstorm()));
  }
}
