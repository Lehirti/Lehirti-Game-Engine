package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.inventory.Consumable;

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
        Text.COCK_SWELLING, new CreviceNorthAfterCucumber()));
  }
}
