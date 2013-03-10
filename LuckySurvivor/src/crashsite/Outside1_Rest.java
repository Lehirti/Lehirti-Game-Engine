package crashsite;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;
import lge.state.TimeInterval;

public class Outside1_Rest extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_WAKE_UP;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(CrashSite.DREAM_AFTER_CRASH);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    addOption(Key.OPTION_ENTER, Text.OPTION_WAKE_UP, new Outside1_MorningAfterCrash());
  }
  
  @Override
  public TimeInterval getRequiredTimeInterval() {
    // advance time by 3 hours (format is DDhhmmss; 24h; but do NOT add leading zeros. 00030000 would be wrong!)
    return TimeInterval.advanceBy(30000);
  }
}
