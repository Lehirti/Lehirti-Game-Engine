package org.lehirti.luckysurvivor.lookouthill;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.SetFlagTextOnlyEvent;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.BoolState;
import org.lehirti.luckysurvivor.map.Map;
import org.lehirti.luckysurvivor.map.Map.Location;

public class MapToLookoutHill extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_SEARCH_FOR_WATER,
    SEARCH_FOR_WATER,
    OPTION_DRING_FROM_SPRING,
    DRING_FROM_SPRING,
    OPTION_SEARCH_SOMETHING_TO_EAT,
    SEARCH_SOMETHING_TO_EAT,
    LEAVE_THE_AREA
  }
  
  public static enum Bool implements BoolState {
    HAS_FOUND_SPRING;
    
    @Override
    public Boolean defaultValue() {
      return Boolean.FALSE;
    }
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(LookoutHill.LOOKOUT_HILL);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    if (is(Bool.HAS_FOUND_SPRING)) {
      addOption(Key.OPTION_NORTH, Text.OPTION_DRING_FROM_SPRING, new TextOnlyEvent(Text.DRING_FROM_SPRING,
          new MapToLookoutHill()));
    } else {
      addOption(Key.OPTION_NORTH, Text.OPTION_SEARCH_FOR_WATER, new SetFlagTextOnlyEvent(Bool.HAS_FOUND_SPRING,
          Text.SEARCH_FOR_WATER, new MapToLookoutHill()));
    }
    addOption(Key.OPTION_SOUTH, Text.OPTION_SEARCH_SOMETHING_TO_EAT, new TextOnlyEvent(Text.SEARCH_SOMETHING_TO_EAT,
        new MapToLookoutHill()));
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Text.LEAVE_THE_AREA, new Map(
        Location.LOOKOUT_HILL)));
  }
}
