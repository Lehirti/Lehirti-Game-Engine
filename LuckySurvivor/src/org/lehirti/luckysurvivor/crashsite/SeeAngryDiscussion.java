package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class SeeAngryDiscussion extends EventNode<NullState> {
  public static enum Text implements TextKey {
	  TEXT_ANGRY_DISCUSSION
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(CrashSite.ANGRY_DISCUSSION);
  }
  
  @Override
  protected void doEvent() {
	 set(CrashSiteBool.HAS_SEEN_ANGRY_DISCUSSION, true);
	  
	 setText(Text.TEXT_ANGRY_DISCUSSION);
     
	 addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new Outside2());
  }
}
