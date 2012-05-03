package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class AreaCrash1_2 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_GO_TO_COCKPIT,
    OPTION_LOOK_FOR_OTHER_SURVIVORS,
    LOOK_FOR_OTHER_SURVIVORS,
    OPTION_SEARCH_RUBBLE_FOR_USEFULL_STUFF,
    SEARCH_RUBBLE_FOR_USEFULL_STUFF,
    OPTION_HELP_FELLOW_SURVIVORS_OUT_OF_PLANE,
    HELP_FELLOW_SURVIVORS_OUT_OF_PLANE,
    OPTION_LEAVE_PLANE,
    OPTION_GO_BACK_TO_YOUR_SEAT;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(AreaCrash.INSIDE_FUSELAGE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Text.OPTION_GO_TO_COCKPIT, new Cockpit());
    addOption(Text.OPTION_LOOK_FOR_OTHER_SURVIVORS,
        new TextOnlyEvent(Text.LOOK_FOR_OTHER_SURVIVORS, new AreaCrash1_2()));
    addOption(Text.OPTION_SEARCH_RUBBLE_FOR_USEFULL_STUFF, new TextOnlyEvent(Text.SEARCH_RUBBLE_FOR_USEFULL_STUFF,
        new AreaCrash1_2()));
    /*
     * TODO addOption(Text.OPTION_HELP_FELLOW_SURVIVORS_OUT_OF_PLANE, new
     * TextOnlyEvent(Text.HELP_FELLOW_SURVIVORS_OUT_OF_PLANE, new AreaCrash1_2()));
     */
    addOption(Text.OPTION_LEAVE_PLANE, new CrashSite1_1());
    addOption(Text.OPTION_GO_BACK_TO_YOUR_SEAT, new AreaCrash1_1());
  }
}
