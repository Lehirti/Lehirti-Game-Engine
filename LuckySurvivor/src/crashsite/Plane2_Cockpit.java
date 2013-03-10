package crashsite;

import lge.events.EventNode;
import lge.events.TextOnlyEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

public class Plane2_Cockpit extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    
    OPTION_CARRY_PILOTS_OUTSIDE,
    CARRY_PILOTS_OUTSIDE,
    CARRY_PILOTS_OUTSIDE_WITH_STRECHER,
    
    OPTION_TRY_TO_USE_RADIO,
    TRY_TO_USE_RADIO,
    
    OPTION_LOOK_FOR_ANYTHING_USEFUL,
    LOOK_FOR_ANYTHING_USEFUL,
    
    OPTION_GO_BACK_TO_FUSELAGE;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(CrashSite.COCKPIT_NON_BURNING);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    TextKey carryPilotsOutText;
    if (is(CrashSiteBoolInventory.STRETCHER)) {
      carryPilotsOutText = Text.CARRY_PILOTS_OUTSIDE_WITH_STRECHER;
    } else {
      carryPilotsOutText = Text.CARRY_PILOTS_OUTSIDE;
    }
    addOption(Key.OPTION_NORTH, Text.OPTION_CARRY_PILOTS_OUTSIDE, new TextOnlyEvent(Key.OPTION_NORTH,
        carryPilotsOutText, new Plane2_Cockpit()));
    addOption(Key.OPTION_WEST, Text.OPTION_TRY_TO_USE_RADIO, new TextOnlyEvent(Key.OPTION_WEST, Text.TRY_TO_USE_RADIO,
        new Plane2_Cockpit()));
    addOption(Key.OPTION_SOUTH, Text.OPTION_LOOK_FOR_ANYTHING_USEFUL, new TextOnlyEvent(Key.OPTION_SOUTH,
        Text.LOOK_FOR_ANYTHING_USEFUL, new Plane2_Cockpit()));
    
    addOption(Key.OPTION_LEAVE, Text.OPTION_GO_BACK_TO_FUSELAGE, new Plane2_Fuselage());
  }
}
