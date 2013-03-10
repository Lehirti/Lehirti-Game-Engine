package peninsulabasinjungle;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

import crashsite.MapToCrashSite;

public class SusanVineRapeAss extends EventNode<NullState> {
  
  public static enum Text implements TextKey {
    DESCRIPTION;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(PeninsulaBasinJungle.SUSAN_VINE_RAPED_ASS);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    // TODO change NPC Yurika stats
    
    addOption(Key.OPTION_EAST, CommonText.OPTION_NEXT, new MapToCrashSite());
  }
}
