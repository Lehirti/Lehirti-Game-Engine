package npc.hazel;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

import crashsite.CrashSite;
import crashsite.MapToCrashSite;

public class MeetHazel extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION_MEET_HAZEL;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CrashSite.OUTSIDE_PLANE_NON_BURNING, Hazel.Image.NAKED);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION_MEET_HAZEL);
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new MapToCrashSite());
  }
}
