package crashsite;

import lge.events.EventNode;
import lge.events.SetFlagTextOnlyEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

public class BuildStrecher extends EventNode<NullState> {
  public static enum Text implements TextKey {
    HAS_MATERIALS_TO_BUILD_STRECHER,
    LACK_MATERIALS_TO_BUILD_STRECHER,
    OBTAINED_STRECHER;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    if (is(CrashSiteBoolInventory.LONG_METAL_POLES) && is(CrashSiteBoolInventory.SHEETS_OF_FABRIC)) {
      setText(Text.HAS_MATERIALS_TO_BUILD_STRECHER);
      addOption(Key.OPTION_EAST, CommonText.OPTION_NEXT, new SetFlagTextOnlyEvent(CrashSiteBoolInventory.STRETCHER,
          Key.OPTION_ENTER, Text.OBTAINED_STRECHER, new Plane2_Fuselage()));
    } else {
      setText(Text.LACK_MATERIALS_TO_BUILD_STRECHER);
      addOption(Key.OPTION_EAST, CommonText.OPTION_NEXT, new Plane2_Fuselage());
    }
  }
}
