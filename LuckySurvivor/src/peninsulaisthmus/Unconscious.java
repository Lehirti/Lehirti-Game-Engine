package peninsulaisthmus;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImageKey;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

public class Unconscious extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DREAM,
    OPTION_AWAKEN
  }
  
  public static enum Img implements ImageKey {
    DREAM,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Img.DREAM);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DREAM);
    addOption(Key.OPTION_EAST, Text.OPTION_AWAKEN, new CreviceFallSite());
  }
}
