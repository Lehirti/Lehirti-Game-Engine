package crashsite;

import lge.events.EventNode;
import lge.events.TextOnlyEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

import common.Rest;

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
