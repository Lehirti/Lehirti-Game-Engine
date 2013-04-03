package intro;

import lge.events.Event.NullState;
import lge.events.EventNode;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;
import lge.state.State;

public class Startscreen extends EventNode<NullState> {
  public static enum Text implements TextKey {
    STARTSCREEN_DESCRIPTION,
    OPTION_START,
    OPTION_INSTRUCTIONS,
    OPTION_THANKS;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(IntroImage.STARTSCREEN);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.STARTSCREEN_DESCRIPTION);
    
    addOption(Key.OPTION_ENTER, Text.OPTION_START, new CheckIn());
    addOption(Key.OPTION_LEAVE, Text.OPTION_INSTRUCTIONS, new Controls());
    addOption(Key.OPTION_WEST, Text.OPTION_THANKS,
        new Credits(Credits.Contributor.values()[State.DIE.nextInt(Credits.Contributor.values().length)]));
  }
}