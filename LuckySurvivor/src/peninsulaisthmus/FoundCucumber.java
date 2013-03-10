package peninsulaisthmus;

import inventory.Consumable;
import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.State;


public class FoundCucumber extends EventNode<NullState> {
  public static enum Text implements TextKey {
    FOUND_ANOTHER_CUCUMBER,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(Consumable.CUCUMBERS);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.FOUND_ANOTHER_CUCUMBER);
    State.change(Consumable.CUCUMBERS, 1); // we add one cucumber to the inventory
    addOption(Key.OPTION_EAST, CommonText.OPTION_NEXT, new CreviceNorth());
  }
}
