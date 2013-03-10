package npc.hazel;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.State;

import crashsite.CrashSite;
import crashsite.MapToCrashSite;

public class SeeHazelNaked extends EventNode<NullState> {
  public static enum Text implements TextKey {
    FIRST_TIME_SLEEPING_HAZEL,
    CONSECUTIVE_TIME_SLEEPING_HAZEL;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CrashSite.SHELTER, Hazel.Image.NAKED_SLEEPING);
  }
  
  @Override
  protected void doEvent() {
    if (State.getEventCount(this) == 1) {
      setText(Text.FIRST_TIME_SLEEPING_HAZEL);
    } else {
      setText(Text.CONSECUTIVE_TIME_SLEEPING_HAZEL);
    }
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new MapToCrashSite());
  }
}
