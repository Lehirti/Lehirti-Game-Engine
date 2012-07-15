package org.lehirti.luckysurvivor.npc.heidi;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.crashsite.CrashSite;
import org.lehirti.luckysurvivor.crashsite.MapToCrashSite;

public class MeetHeidi extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(CrashSite.OUTSIDE_PLANE_NON_BURNING, Heidi.Image.MAIN);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new MapToCrashSite());
  }
}
