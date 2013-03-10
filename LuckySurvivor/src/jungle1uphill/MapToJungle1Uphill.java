package jungle1uphill;

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
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Jungle1Uphill.JUNGLE_1_UPHILL);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    if (is(Bool.HAS_EXAMINED_GROUND)) {
      addOption(Key.OPTION_NORTH, Text.OPTION_BURY_THE_DEAD, new TextOnlyEvent(Key.OPTION_NORTH, Text.BURY_THE_DEAD,
          new MapToJungle1Uphill()));
    } else {
      addOption(Key.OPTION_NORTH, Text.OPTION_EXAMINE_THE_GROUND, new SetFlagTextOnlyEvent(Bool.HAS_EXAMINED_GROUND,
          Key.OPTION_ENTER, Text.EXAMINE_THE_GROUND, new MapToJungle1Uphill()));
    }
    
    addOption(Key.OPTION_SOUTH, Text.OPTION_SEARCH_FOR_SOMETHING_TO_EAT, new TextOnlyEvent(Key.OPTION_SOUTH,
        Text.SEARCH_FOR_SOMETHING_TO_EAT, new MapToJungle1Uphill()));
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Key.OPTION_LEAVE, Text.LEAVE_THE_AREA,
        new Map(Location.JUNGLE_1_UPHILL)));
  }
}
