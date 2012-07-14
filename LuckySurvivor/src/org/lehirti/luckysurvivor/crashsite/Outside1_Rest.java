package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.TimeInterval;

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
