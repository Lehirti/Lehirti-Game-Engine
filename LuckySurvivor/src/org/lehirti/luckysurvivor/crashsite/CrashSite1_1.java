package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.AlternativeTextEvent;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.crashsite.AreaCrash1_2.Bool;

public class CrashSite1_1 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    
    OPTION_LOOK_FOR_OTHER_SURVIVORS,
    LOOK_FOR_OTHER_SURVIVORS,
    LOOK_FOR_OTHER_SURVIVORS_ALT_1,
    LOOK_FOR_OTHER_SURVIVORS_ALT_2,
    LOOK_FOR_OTHER_SURVIVORS_ALT_HAS_HELPED_1,
    LOOK_FOR_OTHER_SURVIVORS_ALT_HAS_HELPED_2,
    
    OPTION_HELP_OTHER_SURVIVORS,
    HELP_OTHER_SURVIVORS,
    HELP_OTHER_SURVIVORS_ALT_1,
    HELP_OTHER_SURVIVORS_ALT_2,
    HELP_OTHER_SURVIVORS_ALT_HAS_HELPED_1,
    HELP_OTHER_SURVIVORS_ALT_HAS_HELPED_2,
    
    OPTION_TALK_TO_OTHER_SURVIVORS,
    TALK_TO_OTHER_SURVIVORS,
    TALK_TO_OTHER_SURVIVORS_ALT_1,
    TALK_TO_OTHER_SURVIVORS_ALT_2,
    TALK_TO_OTHER_SURVIVORS_ALT_HAS_HELPED_1,
    TALK_TO_OTHER_SURVIVORS_ALT_HAS_HELPED_2,
    
    OPTION_REST,
    REST,
    OPTION_LEAVE_THE_AREA,
    LEAVE_THE_AREA,
    OPTION_ENTER_FUSELAGE,
    ENTER_FUSELAGE;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(AreaCrash.OUTSIDE_PLANE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    if (is(Bool.HAS_HELPED_OTHERS_OUT_OF_PLANE)) {
      addOption(Key.OPTION_NORTH, Text.OPTION_LOOK_FOR_OTHER_SURVIVORS, new AlternativeTextEvent(new CrashSite1_1(),
          Text.LOOK_FOR_OTHER_SURVIVORS, Text.LOOK_FOR_OTHER_SURVIVORS_ALT_1, Text.LOOK_FOR_OTHER_SURVIVORS_ALT_2,
          Text.LOOK_FOR_OTHER_SURVIVORS_ALT_HAS_HELPED_1, Text.LOOK_FOR_OTHER_SURVIVORS_ALT_HAS_HELPED_2));
      addOption(Key.OPTION_SOUTH, Text.OPTION_HELP_OTHER_SURVIVORS, new AlternativeTextEvent(new CrashSite1_1(),
          Text.HELP_OTHER_SURVIVORS, Text.HELP_OTHER_SURVIVORS_ALT_1, Text.HELP_OTHER_SURVIVORS_ALT_2,
          Text.HELP_OTHER_SURVIVORS_ALT_HAS_HELPED_1, Text.HELP_OTHER_SURVIVORS_ALT_HAS_HELPED_2));
      addOption(Key.OPTION_EAST, Text.OPTION_TALK_TO_OTHER_SURVIVORS, new AlternativeTextEvent(new CrashSite1_1(),
          Text.TALK_TO_OTHER_SURVIVORS, Text.TALK_TO_OTHER_SURVIVORS_ALT_1, Text.TALK_TO_OTHER_SURVIVORS_ALT_2,
          Text.TALK_TO_OTHER_SURVIVORS_ALT_HAS_HELPED_1, Text.TALK_TO_OTHER_SURVIVORS_ALT_HAS_HELPED_2));
    } else {
      addOption(Key.OPTION_NORTH, Text.OPTION_LOOK_FOR_OTHER_SURVIVORS, new AlternativeTextEvent(new CrashSite1_1(),
          Text.LOOK_FOR_OTHER_SURVIVORS, Text.LOOK_FOR_OTHER_SURVIVORS_ALT_1, Text.LOOK_FOR_OTHER_SURVIVORS_ALT_2));
      addOption(Key.OPTION_SOUTH, Text.OPTION_HELP_OTHER_SURVIVORS, new AlternativeTextEvent(new CrashSite1_1(),
          Text.HELP_OTHER_SURVIVORS, Text.HELP_OTHER_SURVIVORS_ALT_1, Text.HELP_OTHER_SURVIVORS_ALT_2));
      addOption(Key.OPTION_EAST, Text.OPTION_TALK_TO_OTHER_SURVIVORS, new AlternativeTextEvent(new CrashSite1_1(),
          Text.TALK_TO_OTHER_SURVIVORS, Text.TALK_TO_OTHER_SURVIVORS_ALT_1, Text.TALK_TO_OTHER_SURVIVORS_ALT_2));
    }
    
    addOption(Key.OPTION_LEAVE, Text.OPTION_LEAVE_THE_AREA, new TextOnlyEvent(Text.LEAVE_THE_AREA, new CrashSite1_1()));
    addOption(Key.OPTION_ENTER, Text.OPTION_ENTER_FUSELAGE, new TextOnlyEvent(Text.ENTER_FUSELAGE, new AreaCrash1_2()));
    addOption(Key.OPTION_WEST, Text.OPTION_REST, new TextOnlyEvent(Text.REST, new CrashSite2_1()));
  }
}
