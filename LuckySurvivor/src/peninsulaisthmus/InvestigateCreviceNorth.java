package peninsulaisthmus;

import inventory.Consumable;
import lge.events.EventNode;
import lge.events.StandardEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImageKey;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;


public class InvestigateCreviceNorth extends EventNode<NullState> {
  public static enum Text implements TextKey {
    FIND_CUCUMBER_AFTER_INVESTIGATION,
    COCK_SWELLING,
    OPTION_EAT_THE_CUCUMBER
  }
  
  public static enum Img implements ImageKey {
    COCK_SWELLING,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(Consumable.CUCUMBERS);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.FIND_CUCUMBER_AFTER_INVESTIGATION);
    addOption(Key.OPTION_NORTH, Text.OPTION_EAT_THE_CUCUMBER, new StandardEvent(Key.OPTION_NORTH, Img.COCK_SWELLING,
        Text.COCK_SWELLING, new CreviceNorth()));
  }
}
