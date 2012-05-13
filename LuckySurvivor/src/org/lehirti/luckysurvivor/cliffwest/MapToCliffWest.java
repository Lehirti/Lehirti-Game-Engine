package org.lehirti.luckysurvivor.cliffwest;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.map.Map;
import org.lehirti.luckysurvivor.map.Map.Location;

public class MapToCliffWest extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_LOOK_AT_OCEAN,
    LOOK_AT_OCEAN,
    OPTION_SEARCH_SOMETHING_USEFULL,
    SEARCH_SOMETHING_USEFULL,
    OPTION_SEARCH_SOMETHING_TO_EAT,
    SEARCH_SOMETHING_TO_EAT,
    LEAVE_THE_AREA
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CliffWest.CLIFF_WEST);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_LOOK_AT_OCEAN, new TextOnlyEvent(Text.LOOK_AT_OCEAN, new MapToCliffWest()));
    addOption(Key.OPTION_WEST, Text.OPTION_SEARCH_SOMETHING_USEFULL, new TextOnlyEvent(Text.SEARCH_SOMETHING_USEFULL,
        new MapToCliffWest()));
    addOption(Key.OPTION_SOUTH, Text.OPTION_SEARCH_SOMETHING_TO_EAT, new TextOnlyEvent(Text.SEARCH_SOMETHING_TO_EAT,
        new MapToCliffWest()));
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Text.LEAVE_THE_AREA, new Map(
        Location.CLIFF_WEST)));
  }
}
