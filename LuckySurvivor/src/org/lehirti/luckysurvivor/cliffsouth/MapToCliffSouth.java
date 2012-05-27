package org.lehirti.luckysurvivor.cliffsouth;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.map.Map;
import org.lehirti.luckysurvivor.map.Map.Location;

public class MapToCliffSouth extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    LEAVE_THE_AREA
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CliffSouth.CLIFF_SOUTH);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new TextOnlyEvent(Text.LEAVE_THE_AREA, new Map(
        Location.CLIFF_SOUTH)));
  }
}
