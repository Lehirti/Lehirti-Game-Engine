package lookouthill;

import lge.events.EventNode;
import lge.events.SetFlagTextOnlyEvent;
import lge.events.TextOnlyEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.BoolState;
import map.Map;
import map.Map.Location;


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
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(LookoutHill.LOOKOUT_HILL);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    if (is(Bool.HAS_FOUND_SPRING)) {
      addOption(Key.OPTION_NORTH, Text.OPTION_DRING_FROM_SPRING, new TextOnlyEvent(Key.OPTION_NORTH,
          Text.DRING_FROM_SPRING, new MapToLookoutHill()));
    } else {
      addOption(Key.OPTION_NORTH, Text.OPTION_SEARCH_FOR_WATER, new SetFlagTextOnlyEvent(Bool.HAS_FOUND_SPRING,
          Key.OPTION_ENTER, Text.SEARCH_FOR_WATER, new MapToLookoutHill()));
    }
    addOption(Key.OPTION_SOUTH, Text.OPTION_SEARCH_SOMETHING_TO_EAT, new TextOnlyEvent(Key.OPTION_SOUTH,
        Text.SEARCH_SOMETHING_TO_EAT, new MapToLookoutHill()));
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Key.OPTION_LEAVE, Text.LEAVE_THE_AREA,
        new Map(Location.LOOKOUT_HILL)));
  }
}
