package org.lehirti.luckysurvivor.jungle1uphill;

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

public class MapToJungle1Uphill extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_EXAMINE_THE_GROUND,
    EXAMINE_THE_GROUND,
    OPTION_BURY_THE_DEAD,
    BURY_THE_DEAD,
    OPTION_SEARCH_FOR_SOMETHING_TO_EAT,
    SEARCH_FOR_SOMETHING_TO_EAT,
    LEAVE_THE_AREA
  }
  
  public static enum Bool implements BoolState {
    HAS_EXAMINED_GROUND;
    
    @Override
    public Boolean defaultValue() {
      return Boolean.FALSE;
    }
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Jungle1Uphill.JUNGLE_1_UPHILL);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    if (is(Bool.HAS_EXAMINED_GROUND)) {
      addOption(Key.OPTION_NORTH, Text.OPTION_BURY_THE_DEAD, new TextOnlyEvent(Text.BURY_THE_DEAD,
          new MapToJungle1Uphill()));
    } else {
      addOption(Key.OPTION_NORTH, Text.OPTION_EXAMINE_THE_GROUND, new SetFlagTextOnlyEvent(Bool.HAS_EXAMINED_GROUND,
          Text.EXAMINE_THE_GROUND, new MapToJungle1Uphill()));
    }
    
    addOption(Key.OPTION_SOUTH, Text.OPTION_SEARCH_FOR_SOMETHING_TO_EAT, new TextOnlyEvent(
        Text.SEARCH_FOR_SOMETHING_TO_EAT, new MapToJungle1Uphill()));
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Text.LEAVE_THE_AREA, new Map(
        Location.JUNGLE_1_UPHILL)));
  }
}
