package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class Cockpit extends EventNode<NullState> {
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
    return ImgChange.setBG(AreaCrash.COCKPIT);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_CARRY_PILOTS_OUTSIDE, new TextOnlyEvent(Text.CARRY_PILOTS_OUTSIDE,
        new Cockpit()));
    addOption(Key.OPTION_WEST, Text.OPTION_TRY_SALVAGE_ELECTRONICS, new TextOnlyEvent(Text.TRY_SALVALGE_ELECTRONICS,
        new Cockpit()));
    addOption(Key.OPTION_SOUTH, Text.OPTION_LOOK_FOR_ANYTHING_USEFUL, new TextOnlyEvent(Text.LOOK_FOR_ANYTHING_USEFUL,
        new Cockpit()));
    
    addOption(Key.OPTION_LEAVE, Text.OPTION_GO_BACK_TO_FUSELAGE, new AreaCrash1_2());
  }
}
