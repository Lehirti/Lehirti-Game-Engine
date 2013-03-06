package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.common.Rest;

public class Shelter extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    
    OPTION_REST,
    REST,
    
    OPTION_TRY_TO_PEEK_ON_GIRLS,
    TRY_TO_PEEK_ON_GIRLS,
    
    OPTION_EXAMINE_OTHER_SURVIVORS,
    EXAMINE_OTHER_SURVIVORS,
    
    OPTION_GO_OUTSIDE;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CrashSite.SHELTER);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_REST, new Rest(Key.OPTION_NORTH, Text.REST, null, new Shelter()));
    addOption(Key.OPTION_SOUTH, Text.OPTION_TRY_TO_PEEK_ON_GIRLS, new TextOnlyEvent(Key.OPTION_SOUTH,
        Text.TRY_TO_PEEK_ON_GIRLS, new Shelter()));
    addOption(Key.OPTION_EAST, Text.OPTION_EXAMINE_OTHER_SURVIVORS, new TextOnlyEvent(Key.OPTION_EAST,
        Text.EXAMINE_OTHER_SURVIVORS, new Shelter()));
    
    addOption(Key.OPTION_LEAVE, Text.OPTION_GO_OUTSIDE, new MapToCrashSite());
  }
}
