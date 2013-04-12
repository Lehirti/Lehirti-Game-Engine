package crashsite;

import lge.events.Event.NullState;
import lge.events.EventNode;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;
import lge.state.TimeInterval;

public class Outside1_MorningAfterCrashJoinOthers extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_JOIN_OTHERS;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(CrashSite.OUTSIDE_PLANE_MORNING);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    addOption(Key.OPTION_ENTER, Text.OPTION_JOIN_OTHERS, new FirstDiscussion());
  }
  
  
}
