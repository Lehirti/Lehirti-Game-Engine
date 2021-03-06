package crashsite;

import lge.events.EventNode;
import lge.events.TextOnlyEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

public class Plane1_YourSeat extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_CHECK_PASSENGERS_NEXT_TO_YOU,
    CHECK_PASSENGERS_NEXT_TO_YOU,
    OPTION_LOOK_FOR_YOUR_BAGGAGE,
    LOOK_FOR_YOUR_BAGGAGE,
    OPTION_REST_FOR_A_WHILE,
    REST_FOR_A_WHILE,
    OPTION_CHECK_YOURSELF_FOR_INJURIES,
    CHECK_YOURSELF_FOR_INJURIES,
    OPTION_LEAVE_YOUR_SEAT,
    LEAVE_YOUR_SEAT;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CrashSite.IN_OWN_SEAT_IN_PLANE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_CHECK_PASSENGERS_NEXT_TO_YOU, new TextOnlyEvent(Key.OPTION_NORTH,
        Text.CHECK_PASSENGERS_NEXT_TO_YOU, new Plane1_YourSeat()));
    addOption(Key.OPTION_WEST, Text.OPTION_LOOK_FOR_YOUR_BAGGAGE, new TextOnlyEvent(Key.OPTION_WEST,
        Text.LOOK_FOR_YOUR_BAGGAGE, new Plane1_YourSeat()));
    addOption(Key.OPTION_SOUTH, Text.OPTION_REST_FOR_A_WHILE, new TextOnlyEvent(Key.OPTION_SOUTH,
        Text.REST_FOR_A_WHILE, new Plane1_YourSeat()));
    addOption(Key.OPTION_EAST, Text.OPTION_CHECK_YOURSELF_FOR_INJURIES, new TextOnlyEvent(Key.OPTION_EAST,
        Text.CHECK_YOURSELF_FOR_INJURIES, new Plane1_YourSeat()));
    
    if (is(CrashSiteBoolInventory.WATCH)) {
      addOption(Key.OPTION_LEAVE, Text.OPTION_LEAVE_YOUR_SEAT, new TextOnlyEvent(Key.OPTION_LEAVE,
          Text.LEAVE_YOUR_SEAT, new Plane1_Fuselage()));
    } else {
      addOption(Key.OPTION_LEAVE, Text.OPTION_LEAVE_YOUR_SEAT, new FindWatch());
    }
  }
}
