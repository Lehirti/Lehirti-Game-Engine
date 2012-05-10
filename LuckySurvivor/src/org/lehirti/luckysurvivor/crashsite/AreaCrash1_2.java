package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.SetFlagEvent;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.BoolState;

public class AreaCrash1_2 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_GO_TO_COCKPIT,
    OPTION_LOOK_FOR_OTHER_SURVIVORS,
    LOOK_FOR_OTHER_SURVIVORS,
    OPTION_LOOK_FOR_MORE_SURVIVORS,
    LOOK_FOR_MORE_SURVIVORS,
    OPTION_SEARCH_RUBBLE_FOR_USEFULL_STUFF,
    SEARCH_RUBBLE_FOR_USEFULL_STUFF,
    OPTION_HELP_FELLOW_SURVIVORS_OUT_OF_PLANE,
    HELP_FELLOW_SURVIVORS_OUT_OF_PLANE,
    OPTION_LEAVE_PLANE,
    LEAVE_PLANE,
    OPTION_GO_BACK_TO_YOUR_SEAT,
    GO_BACK_TO_YOUR_SEAT;
  }
  
  public static enum Bool implements BoolState {
    HAS_LOOKED_FOR_SURVIVORS_AT_CRASH_AREA_1_2,
    HAS_HELPED_OTHERS_OUT_OF_PLANE;
    
    @Override
    public Boolean defaultValue() {
      return Boolean.FALSE;
    }
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(AreaCrash.INSIDE_FUSELAGE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_GO_TO_COCKPIT, new Cockpit());
    
    if (is(Bool.HAS_LOOKED_FOR_SURVIVORS_AT_CRASH_AREA_1_2)) {
      addOption(Key.OPTION_WEST, Text.OPTION_LOOK_FOR_MORE_SURVIVORS, new TextOnlyEvent(Text.LOOK_FOR_MORE_SURVIVORS,
          new AreaCrash1_2()));
    } else {
      addOption(Key.OPTION_WEST, Text.OPTION_LOOK_FOR_OTHER_SURVIVORS, new SetFlagEvent(
          Bool.HAS_LOOKED_FOR_SURVIVORS_AT_CRASH_AREA_1_2, Text.LOOK_FOR_OTHER_SURVIVORS, new AreaCrash1_2()));
    }
    
    addOption(Key.OPTION_SOUTH, Text.OPTION_SEARCH_RUBBLE_FOR_USEFULL_STUFF, new TextOnlyEvent(
        Text.SEARCH_RUBBLE_FOR_USEFULL_STUFF, new AreaCrash1_2()));
    
    if (is(Bool.HAS_LOOKED_FOR_SURVIVORS_AT_CRASH_AREA_1_2)) {
      addOption(Key.OPTION_EAST, Text.OPTION_HELP_FELLOW_SURVIVORS_OUT_OF_PLANE, new SetFlagEvent(
          Bool.HAS_HELPED_OTHERS_OUT_OF_PLANE, Text.HELP_FELLOW_SURVIVORS_OUT_OF_PLANE, new AreaCrash1_2()));
    }
    
    addOption(Key.OPTION_LEAVE, Text.OPTION_LEAVE_PLANE, new TextOnlyEvent(Text.LEAVE_PLANE, new CrashSite1_1()));
    
    addOption(Key.OPTION_ENTER, Text.OPTION_GO_BACK_TO_YOUR_SEAT, new TextOnlyEvent(Text.GO_BACK_TO_YOUR_SEAT,
        new AreaCrash1_1()));
  }
}
