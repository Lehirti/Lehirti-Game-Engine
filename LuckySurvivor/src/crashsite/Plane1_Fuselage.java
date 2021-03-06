package crashsite;

import lge.events.EventNode;
import lge.events.SetFlagEvent;
import lge.events.SetFlagTextOnlyEvent;
import lge.events.StandardEvent;
import lge.events.TextOnlyEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;
import lge.state.BoolState;

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
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CrashSite.INSIDE_FUSELAGE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_GO_TO_COCKPIT, new Plane1_Cockpit());
    
    if (is(Bool.HAS_LOOKED_FOR_SURVIVORS)) {
      addOption(Key.OPTION_WEST, Text.OPTION_LOOK_FOR_MORE_SURVIVORS, new StandardEvent(Key.OPTION_WEST,
          CrashSite.LOOK_FOR_SURVIVORS_AFTER_CRASH, Text.LOOK_FOR_MORE_SURVIVORS, new Plane1_Fuselage()));
    } else {
      addOption(Key.OPTION_WEST, Text.OPTION_LOOK_FOR_OTHER_SURVIVORS, new SetFlagEvent(Bool.HAS_LOOKED_FOR_SURVIVORS,
          CrashSite.LOOK_FOR_SURVIVORS_AFTER_CRASH, Text.LOOK_FOR_OTHER_SURVIVORS, new Plane1_Fuselage()));
    }
    
    addOption(Key.OPTION_SOUTH, Text.OPTION_SEARCH_RUBBLE_FOR_USEFULL_STUFF, new TextOnlyEvent(Key.OPTION_SOUTH,
        Text.SEARCH_RUBBLE_FOR_USEFULL_STUFF, new Plane1_Fuselage()));
    
    if (is(Bool.HAS_LOOKED_FOR_SURVIVORS)) {
      addOption(Key.OPTION_EAST, Text.OPTION_HELP_FELLOW_SURVIVORS_OUT_OF_PLANE, new SetFlagTextOnlyEvent(
          Bool.HAS_HELPED_OTHERS_OUT_OF_PLANE, Key.OPTION_ENTER, Text.HELP_FELLOW_SURVIVORS_OUT_OF_PLANE, new Plane1_Fuselage()));
    }
    
    addOption(Key.OPTION_LEAVE, Text.OPTION_LEAVE_PLANE, new TextOnlyEvent(Key.OPTION_LEAVE, Text.LEAVE_PLANE,
        new Outside1()));
    
    addOption(Key.OPTION_ENTER, Text.OPTION_GO_BACK_TO_YOUR_SEAT, new TextOnlyEvent(Key.OPTION_ENTER,
        Text.GO_BACK_TO_YOUR_SEAT, new Plane1_YourSeat()));
  }
}
