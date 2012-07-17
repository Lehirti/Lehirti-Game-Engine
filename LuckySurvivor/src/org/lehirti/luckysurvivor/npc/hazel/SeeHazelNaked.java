package org.lehirti.luckysurvivor.npc.hazel;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.State;
import org.lehirti.luckysurvivor.crashsite.CrashSite;
import org.lehirti.luckysurvivor.crashsite.MapToCrashSite;

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
