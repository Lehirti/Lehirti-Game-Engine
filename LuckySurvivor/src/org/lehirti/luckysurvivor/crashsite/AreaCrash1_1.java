package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class AreaCrash1_1 extends EventNode<NullState> {
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
    OPTION_LEAVE_YOUR_SEAT;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(AreaCrash.IN_OWN_SEAT_IN_PLANE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Text.OPTION_CHECK_PASSENGERS_NEXT_TO_YOU, new TextOnlyEvent(Text.CHECK_PASSENGERS_NEXT_TO_YOU,
        new AreaCrash1_1()));
    addOption(Text.OPTION_LOOK_FOR_YOUR_BAGGAGE, new TextOnlyEvent(Text.LOOK_FOR_YOUR_BAGGAGE, new AreaCrash1_1()));
    addOption(Text.OPTION_REST_FOR_A_WHILE, new TextOnlyEvent(Text.REST_FOR_A_WHILE, new AreaCrash1_1()));
    addOption(Text.OPTION_CHECK_YOURSELF_FOR_INJURIES, new TextOnlyEvent(Text.CHECK_YOURSELF_FOR_INJURIES,
        new AreaCrash1_1()));
    addOption(Text.OPTION_LEAVE_YOUR_SEAT, new AreaCrash1_2());
  }
}
