package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.SetFlagEvent;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.BoolState;

public class Plane1_Fuselage extends EventNode<NullState> {
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
    HAS_LOOKED_FOR_SURVIVORS,
    HAS_HELPED_OTHERS_OUT_OF_PLANE;
    
    @Override
    public Boolean defaultValue() {
      return Boolean.FALSE;
    }
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(CrashSite.INSIDE_FUSELAGE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_GO_TO_COCKPIT, new Plane1_Cockpit());
    
    if (is(Bool.HAS_LOOKED_FOR_SURVIVORS)) {
      addOption(Key.OPTION_WEST, Text.OPTION_LOOK_FOR_MORE_SURVIVORS, new TextOnlyEvent(Text.LOOK_FOR_MORE_SURVIVORS,
          new Plane1_Fuselage()));
    } else {
      addOption(Key.OPTION_WEST, Text.OPTION_LOOK_FOR_OTHER_SURVIVORS, new SetFlagEvent(
          Bool.HAS_LOOKED_FOR_SURVIVORS, Text.LOOK_FOR_OTHER_SURVIVORS, new Plane1_Fuselage()));
    }
    
    addOption(Key.OPTION_SOUTH, Text.OPTION_SEARCH_RUBBLE_FOR_USEFULL_STUFF, new TextOnlyEvent(
        Text.SEARCH_RUBBLE_FOR_USEFULL_STUFF, new Plane1_Fuselage()));
    
    if (is(Bool.HAS_LOOKED_FOR_SURVIVORS)) {
      addOption(Key.OPTION_EAST, Text.OPTION_HELP_FELLOW_SURVIVORS_OUT_OF_PLANE, new SetFlagEvent(
          Bool.HAS_HELPED_OTHERS_OUT_OF_PLANE, Text.HELP_FELLOW_SURVIVORS_OUT_OF_PLANE, new Plane1_Fuselage()));
    }
    
    addOption(Key.OPTION_LEAVE, Text.OPTION_LEAVE_PLANE, new TextOnlyEvent(Text.LEAVE_PLANE, new Outside1()));
    
    addOption(Key.OPTION_ENTER, Text.OPTION_GO_BACK_TO_YOUR_SEAT, new TextOnlyEvent(Text.GO_BACK_TO_YOUR_SEAT,
        new Plane1_YourSeat()));
  }
}
