package intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

import crashsite.Plane1_YourSeat;

public class TheCrash extends EventNode<NullState> {
  public static enum Text implements TextKey {
    THE_CRASH,
    OPTION_TUG_YOUR_HEAD;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(IntroImage.THE_CRASH);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.THE_CRASH);
    
    addOption(Key.OPTION_WEST, Text.OPTION_TUG_YOUR_HEAD, new Plane1_YourSeat());
  }
}
