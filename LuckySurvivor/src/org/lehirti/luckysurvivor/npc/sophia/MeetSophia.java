package org.lehirti.luckysurvivor.npc.sophia;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.crashsite.CrashSite;
import org.lehirti.luckysurvivor.crashsite.MapToCrashSite;

public class MeetSophia extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION_MEET_SOPHIA;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CrashSite.OUTSIDE_PLANE_NON_BURNING, Sophia.Image.MAIN);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION_MEET_SOPHIA);
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new MapToCrashSite());
  }
}
