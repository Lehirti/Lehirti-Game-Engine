package crashsite;

import lge.events.AlternativeOneTimeEvents;
import lge.events.EventNode;
import lge.events.TextOnlyEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.TextAndImageKeyWithFlag;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

import crashsite.Plane1_Fuselage.Bool;

public class Outside1 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    
    OPTION_LOOK_FOR_OTHER_SURVIVORS,
    
    OPTION_HELP_OTHER_SURVIVORS,
    
    OPTION_TALK_TO_OTHER_SURVIVORS,
    
    OPTION_REST,
    REST,
    OPTION_LEAVE_THE_AREA,
    LEAVE_THE_AREA,
    OPTION_ENTER_FUSELAGE,
    ENTER_FUSELAGE;
  }
  
  public static enum TxtImg implements TextAndImageKeyWithFlag {
    LOOK_FOR_OTHER_SURVIVORS_DEFAULT,
    LOOK_FOR_OTHER_SURVIVORS_ALT_1,
    LOOK_FOR_OTHER_SURVIVORS_ALT_2,
    LOOK_FOR_OTHER_SURVIVORS_ALT_HAS_HELPED_1,
    LOOK_FOR_OTHER_SURVIVORS_ALT_HAS_HELPED_2,
    
    HELP_OTHER_SURVIVORS_DEFAULT,
    HELP_OTHER_SURVIVORS_ALT_1,
    HELP_OTHER_SURVIVORS_ALT_2,
    HELP_OTHER_SURVIVORS_ALT_HAS_HELPED_1,
    HELP_OTHER_SURVIVORS_ALT_HAS_HELPED_2,
    
    TALK_TO_OTHER_SURVIVORS_DEFAULT,
    TALK_TO_OTHER_SURVIVORS_ALT_1,
    TALK_TO_OTHER_SURVIVORS_ALT_2,
    TALK_TO_OTHER_SURVIVORS_ALT_HAS_HELPED_1,
    TALK_TO_OTHER_SURVIVORS_ALT_HAS_HELPED_2;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CrashSite.OUTSIDE_PLANE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    if (is(Bool.HAS_HELPED_OTHERS_OUT_OF_PLANE)) {
      addOption(Key.OPTION_NORTH, Text.OPTION_LOOK_FOR_OTHER_SURVIVORS, new AlternativeOneTimeEvents(new Outside1(),
          TxtImg.LOOK_FOR_OTHER_SURVIVORS_DEFAULT, TxtImg.LOOK_FOR_OTHER_SURVIVORS_ALT_1,
          TxtImg.LOOK_FOR_OTHER_SURVIVORS_ALT_2, TxtImg.LOOK_FOR_OTHER_SURVIVORS_ALT_HAS_HELPED_1,
          TxtImg.LOOK_FOR_OTHER_SURVIVORS_ALT_HAS_HELPED_2));
      addOption(Key.OPTION_SOUTH, Text.OPTION_HELP_OTHER_SURVIVORS, new AlternativeOneTimeEvents(new Outside1(),
          TxtImg.HELP_OTHER_SURVIVORS_DEFAULT, TxtImg.HELP_OTHER_SURVIVORS_ALT_1, TxtImg.HELP_OTHER_SURVIVORS_ALT_2,
          TxtImg.HELP_OTHER_SURVIVORS_ALT_HAS_HELPED_1, TxtImg.HELP_OTHER_SURVIVORS_ALT_HAS_HELPED_2));
      addOption(Key.OPTION_EAST, Text.OPTION_TALK_TO_OTHER_SURVIVORS, new AlternativeOneTimeEvents(new Outside1(),
          TxtImg.TALK_TO_OTHER_SURVIVORS_DEFAULT, TxtImg.TALK_TO_OTHER_SURVIVORS_ALT_1,
          TxtImg.TALK_TO_OTHER_SURVIVORS_ALT_2, TxtImg.TALK_TO_OTHER_SURVIVORS_ALT_HAS_HELPED_1,
          TxtImg.TALK_TO_OTHER_SURVIVORS_ALT_HAS_HELPED_2));
    } else {
      addOption(Key.OPTION_NORTH, Text.OPTION_LOOK_FOR_OTHER_SURVIVORS, new AlternativeOneTimeEvents(new Outside1(),
          TxtImg.LOOK_FOR_OTHER_SURVIVORS_DEFAULT, TxtImg.LOOK_FOR_OTHER_SURVIVORS_ALT_1,
          TxtImg.LOOK_FOR_OTHER_SURVIVORS_ALT_2));
      addOption(Key.OPTION_SOUTH, Text.OPTION_HELP_OTHER_SURVIVORS, new AlternativeOneTimeEvents(new Outside1(),
          TxtImg.HELP_OTHER_SURVIVORS_DEFAULT, TxtImg.HELP_OTHER_SURVIVORS_ALT_1, TxtImg.HELP_OTHER_SURVIVORS_ALT_2));
      addOption(Key.OPTION_EAST, Text.OPTION_TALK_TO_OTHER_SURVIVORS, new AlternativeOneTimeEvents(new Outside1(),
          TxtImg.TALK_TO_OTHER_SURVIVORS_DEFAULT, TxtImg.TALK_TO_OTHER_SURVIVORS_ALT_1,
          TxtImg.TALK_TO_OTHER_SURVIVORS_ALT_2));
    }
    
    addOption(Key.OPTION_LEAVE, Text.OPTION_LEAVE_THE_AREA, new TextOnlyEvent(Key.OPTION_LEAVE, Text.LEAVE_THE_AREA,
        new Outside1()));
    addOption(Key.OPTION_ENTER, Text.OPTION_ENTER_FUSELAGE, new TextOnlyEvent(Key.OPTION_ENTER, Text.ENTER_FUSELAGE,
        new Plane1_Fuselage()));
    addOption(Key.OPTION_WEST, Text.OPTION_REST, new TextOnlyEvent(Key.OPTION_WEST, Text.REST, new Outside1_Rest()));
  }
}
