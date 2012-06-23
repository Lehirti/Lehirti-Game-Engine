package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class Outside1_MorningAfterCrash extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_GET_UP;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(CrashSite.OUTSIDE_PLANE_MORNING);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    addOption(Key.OPTION_ENTER, Text.OPTION_GET_UP, new MapToCrashSite());
  }
}
