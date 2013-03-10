package peninsulabasinjungle;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

import crashsite.MapToCrashSite;

public class SusanVineRapeFace extends EventNode<NullState> {
  
  public static enum Text implements TextKey {
    DESCRIPTION;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(PeninsulaBasinJungle.SUSAN_VINE_RAPED_FACE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    // TODO change NPC Yurika stats
    
    addOption(Key.OPTION_NORTH, CommonText.OPTION_NEXT, new MapToCrashSite());
  }
}
