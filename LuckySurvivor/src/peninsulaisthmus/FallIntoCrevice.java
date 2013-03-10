package peninsulaisthmus;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImageKey;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

public class FallIntoCrevice extends EventNode<NullState> {
  public static enum Text implements TextKey {
    FALL,
    OPTION_GOOD_BY_CRUEL_WORLD
  }
  
  public static enum Img implements ImageKey {
    FALL,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Img.FALL);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.FALL);
    addOption(Key.OPTION_EAST, Text.OPTION_GOOD_BY_CRUEL_WORLD, new Unconscious());
  }
}
