package npc.heidi;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

import crashsite.CrashSite;
import crashsite.MapToCrashSite;

public class MeetHeidi extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION_MEET_HEIDI;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CrashSite.OUTSIDE_PLANE_NON_BURNING, Heidi.Image.MAIN);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION_MEET_HEIDI);
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new MapToCrashSite());
  }
}
