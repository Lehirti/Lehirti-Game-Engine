package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class CrashSite1_1 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_LOOK_FOR_OTHER_SURVIVORS,
    LOOK_FOR_OTHER_SURVIVORS,
    OPTION_REST,
    REST,
    OPTION_LEAVE_THE_AREA,
    OPTION_ENTER_FUSELAGE;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(AreaCrash.INSIDE_FUSELAGE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Text.OPTION_LOOK_FOR_OTHER_SURVIVORS,
        new TextOnlyEvent(Text.LOOK_FOR_OTHER_SURVIVORS, new CrashSite1_1()));
    addOption(Text.OPTION_REST, new TextOnlyEvent(Text.REST, new CrashSite1_1()));
    // TODO addOption(Text.OPTION_LEAVE_THE_AREA, new CrashSite1_1());
    addOption(Text.OPTION_ENTER_FUSELAGE, new AreaCrash1_2());
  }
}
