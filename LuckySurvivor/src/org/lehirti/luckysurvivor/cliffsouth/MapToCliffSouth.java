package org.lehirti.luckysurvivor.cliffsouth;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.SetFlagEvent;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.BoolState;
import org.lehirti.luckysurvivor.map.Map;
import org.lehirti.luckysurvivor.map.Map.Location;

public class MapToCliffSouth extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_EXAMINE_THE_GROUND,
    EXAMINE_THE_GROUND,
    OPTION_BURY_THE_DEAD,
    BURY_THE_DEAD,
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
    return ImgChange.setBGAndFG(CliffSouth.CLIFF_SOUTH);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    if (is(Bool.HAS_EXAMINED_GROUND)) {
      addOption(Key.OPTION_NORTH, Text.OPTION_BURY_THE_DEAD, new TextOnlyEvent(Text.BURY_THE_DEAD,
          new MapToCliffSouth()));
    } else {
      addOption(Key.OPTION_NORTH, Text.OPTION_EXAMINE_THE_GROUND, new SetFlagEvent(Bool.HAS_EXAMINED_GROUND,
          Text.EXAMINE_THE_GROUND, new MapToCliffSouth()));
    }
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Text.LEAVE_THE_AREA, new Map(
        Location.CLIFF_SOUTH)));
  }
}
