package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.State;
import org.lehirti.luckysurvivor.inventory.Consumable;

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
