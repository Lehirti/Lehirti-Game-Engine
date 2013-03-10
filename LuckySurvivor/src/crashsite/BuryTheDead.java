package crashsite;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

public class BuryTheDead extends EventNode<NullState> {
  public static enum Text implements TextKey {
    BURY_WITH_SHOVEL,
    BURY_WITHOUT_SHOVEL;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    if (is(CrashSiteBoolInventory.SHOVEL)) {
      setText(Text.BURY_WITH_SHOVEL);
    } else {
      setText(Text.BURY_WITHOUT_SHOVEL);
    }
    
    addOption(Key.OPTION_EAST, CommonText.OPTION_NEXT, new MapToCrashSite());
  }
}
