package org.lehirti.luckysurvivor.cliffwest;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.State;
import org.lehirti.luckysurvivor.map.Map;
import org.lehirti.luckysurvivor.map.Map.Location;
import org.lehirti.luckysurvivor.npc.NPCSelectEvent;

/**
 * this is the default first event when entering CliffWest
 */
public class MapToCliffWest extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_LOOK_AT_OCEAN,
    LOOK_AT_OCEAN,
    OPTION_SEARCH_SOMETHING_USEFULL,
    SEARCH_SOMETHING_USEFULL,
    OPTION_SEARCH_SOMETHING_TO_EAT,
    SEARCH_SOMETHING_TO_EAT,
    LEAVE_THE_AREA,
    OPTION_EXAMINE_THE_GIRLS
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CliffWest.CLIFF_WEST);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_LOOK_AT_OCEAN, new TextOnlyEvent(Key.OPTION_NORTH, Text.LOOK_AT_OCEAN,
        new MapToCliffWest()));
    addOption(Key.OPTION_WEST, Text.OPTION_SEARCH_SOMETHING_USEFULL, new TextOnlyEvent(Key.OPTION_WEST,
        Text.SEARCH_SOMETHING_USEFULL, new MapToCliffWest()));
    addOption(Key.OPTION_SOUTH, Text.OPTION_SEARCH_SOMETHING_TO_EAT, new TextOnlyEvent(Key.OPTION_SOUTH,
        Text.SEARCH_SOMETHING_TO_EAT, new MapToCliffWest()));
    
    /*
     * only after we have met Jordan, will we be able to examine the girls here
     */
    if (State.getEventCount(MeetJordanForTheFirstTime.class) > 0) {
      addOption(Key.OPTION_EAST, Text.OPTION_EXAMINE_THE_GIRLS, new NPCSelectEvent(CliffWest.CLIFF_WEST,
          new MapToCliffWest(), CliffWestNPCs.getNPCs(), 0));
    }
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Key.OPTION_LEAVE, Text.LEAVE_THE_AREA,
        new Map(Location.CLIFF_WEST)));
  }
}
