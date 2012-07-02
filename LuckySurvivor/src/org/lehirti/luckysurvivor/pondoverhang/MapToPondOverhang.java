package org.lehirti.luckysurvivor.pondoverhang;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.TextAndImageKeyWithFlag;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.State;
import org.lehirti.luckysurvivor.map.Map;
import org.lehirti.luckysurvivor.map.Map.Location;

public class MapToPondOverhang extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    LEAVE_THE_AREA,
    OPTION_OOGLE,
    OPTION_TAKE_A_BATH,
  }
  
public static enum TxtImg implements TextAndImageKeyWithFlag {
	    BATHING_GIRL_1,
	    BATHING_GIRL_2,
	    TAKE_A_BATH,
}
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(PondOverhang.POND_OVERHANG);
  }

  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    //I Try to implement this option only if the (repeatable) GILRS_PRESENT event happened
    if (is(PondOverhangBool.GIRLS_PRESENT)) {
        addOption(Key.OPTION_NORTH, Text.OPTION_OOGLE, new StandardEvent(
            new MapToPondOverhang(), TxtImg.BATHING_GIRL_1, TxtImg.BATHING_GIRL_2));
    }
    //If no girl is present, you can bathe alone
         else {
        addOption(Key.OPTION_NORTH, Text.OPTION_TAKE_A_BATH, new StandardEvent(
            new MapToPondOverhang(), TxtImg.TAKE_A_BATH));
    
    
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Key.OPTION_LEAVE, Text.LEAVE_THE_AREA,
        new Map(Location.POND_OVERHANG)));
  }
 }
}
