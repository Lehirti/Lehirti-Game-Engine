package org.lehirti.luckysurvivor.npc.emily;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.cliffwest.CliffWest;
import org.lehirti.luckysurvivor.crashsite.MapToCrashSite;

public class MeetEmily extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CliffWest.CLIFF_WEST, Emily.Image.MAIN);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new MapToCrashSite());
  }
}
