package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.map.Map;
import org.lehirti.luckysurvivor.map.Map.Location;

/**
 * this is the default first event when entering
 */
public class MapToPeninsulaIsthmus extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    LEAVE_THE_AREA,
    OPTION_SEARCH_FOR_A_WAY,
    SEARCH_FOR_A_WAY
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(PeninsulaIsthmus.PENINSULA_ISTHMUS);
  }
  
  @Override
  protected void doEvent() {
    /*
     * set "HAS_BEEN_HERE_BEFORE" flag to true;
     */
    set(PeninsulaIsthmusBool.HAS_NOT_BEEN_HERE_BEFORE, false);
    
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_WEST, Text.OPTION_SEARCH_FOR_A_WAY, new TextOnlyEvent(Key.OPTION_WEST, Text.SEARCH_FOR_A_WAY,
            new MapToPeninsulaIsthmus()));
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Key.OPTION_LEAVE, Text.LEAVE_THE_AREA,
        new Map(Location.PENINSULA_ISTHMUS)));
  }
}
