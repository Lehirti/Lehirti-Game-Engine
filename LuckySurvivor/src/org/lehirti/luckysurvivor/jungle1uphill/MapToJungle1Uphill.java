package org.lehirti.luckysurvivor.jungle1uphill;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.map.Map;
import org.lehirti.luckysurvivor.map.Map.Location;

public class MapToJungle1Uphill extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_SEARCH_FOR_SOMETHING_TO_EAT,
    SEARCH_FOR_SOMETHING_TO_EAT,
    LEAVE_THE_AREA
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Jungle1Uphill.JUNGLE_1_UPHILL);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_SEARCH_FOR_SOMETHING_TO_EAT, new TextOnlyEvent(
        Text.SEARCH_FOR_SOMETHING_TO_EAT, new MapToJungle1Uphill()));
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Text.LEAVE_THE_AREA, new Map(
        Location.JUNGLE_1_UPHILL)));
  }
}
