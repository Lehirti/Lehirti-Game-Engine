package crashsite;

import lge.events.EventNode;
import lge.events.TextOnlyEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

public class Plane1_Cockpit extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_CARRY_PILOTS_OUTSIDE,
    CARRY_PILOTS_OUTSIDE,
    OPTION_TRY_SALVAGE_ELECTRONICS,
    TRY_SALVALGE_ELECTRONICS,
    OPTION_LOOK_FOR_ANYTHING_USEFUL,
    LOOK_FOR_ANYTHING_USEFUL,
    OPTION_GO_BACK_TO_FUSELAGE;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(CrashSite.COCKPIT);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_CARRY_PILOTS_OUTSIDE, new TextOnlyEvent(Key.OPTION_NORTH,
        Text.CARRY_PILOTS_OUTSIDE, new Plane1_Cockpit()));
    addOption(Key.OPTION_WEST, Text.OPTION_TRY_SALVAGE_ELECTRONICS, new TextOnlyEvent(Key.OPTION_WEST,
        Text.TRY_SALVALGE_ELECTRONICS, new Plane1_Cockpit()));
    addOption(Key.OPTION_SOUTH, Text.OPTION_LOOK_FOR_ANYTHING_USEFUL, new TextOnlyEvent(Key.OPTION_SOUTH,
        Text.LOOK_FOR_ANYTHING_USEFUL, new Plane1_Cockpit()));
    
    addOption(Key.OPTION_LEAVE, Text.OPTION_GO_BACK_TO_FUSELAGE, new Plane1_Fuselage());
  }
}
