package crashsite;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

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
	 setText(Text.TEXT_ANGRY_DISCUSSION);
     
	 addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new MapToCrashSite());
  }
}
