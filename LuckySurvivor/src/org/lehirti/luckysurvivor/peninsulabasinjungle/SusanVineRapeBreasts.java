package org.lehirti.luckysurvivor.peninsulabasinjungle;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.crashsite.MapToCrashSite;

public class SusanVineRapeBreasts extends EventNode<NullState> {
  
  public static enum Text implements TextKey {
    DESCRIPTION;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(PeninsulaBasinJungle.SUSAN_VINE_RAPED_BREASTS);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    // TODO change NPC Yurika stats
    
    addOption(Key.OPTION_WEST, CommonText.OPTION_NEXT, new MapToCrashSite());
  }
}
