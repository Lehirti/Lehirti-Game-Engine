package intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

public class Bus extends EventNode<NullState> {
  public static enum Text implements TextKey {
    BUS_DESCRIPTION,
    OPTION_ENTER_PLANE;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(IntroImage.BUS);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.BUS_DESCRIPTION);
    
    addOption(Key.OPTION_ENTER, Text.OPTION_ENTER_PLANE, new BoardPlane());
  }
}
