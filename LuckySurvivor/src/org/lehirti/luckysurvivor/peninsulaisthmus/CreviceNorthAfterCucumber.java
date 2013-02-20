package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.State;

public class CreviceNorthAfterCucumber extends EventNode<NullState> {
  public static enum Text implements TextKey {
    EJACULATION_FROM_EFFECTS_OF_CUCUMBER,
    THIS_IS_WHERE_YOU_FOUND_THE_CUCUMBER,
    OPTION_SEARCH_FOR_CUCUMBERS,
  }
  
  public static enum Img implements ImageKey {
    EJACULATION_FROM_EFFECTS_OF_CUCUMBER
  }
  
  @Override
  protected ImgChange updateImageArea() {
    if (State.getEventCount(this) == 0) {
      return ImgChange.setBGAndFG(CreviceNorth.Img.CREVICE_NORTH, Img.EJACULATION_FROM_EFFECTS_OF_CUCUMBER);
    } else {
      return ImgChange.setBG(CreviceNorth.Img.CREVICE_NORTH);
    }
  }
  
  @Override
  protected void doEvent() {
    if (State.getEventCount(this) == 1) {
      setText(Text.EJACULATION_FROM_EFFECTS_OF_CUCUMBER);
    } else {
      setText(Text.THIS_IS_WHERE_YOU_FOUND_THE_CUCUMBER);
    }
    if (State.getEventCount(FoundCucumber.class) == 0) {
      addOption(Key.OPTION_EAST, Text.OPTION_SEARCH_FOR_CUCUMBERS, new FoundCucumber());
    }
    addOption(Key.OPTION_NORTH, CommonText.OPTION_GO_NORTH, new CreviceFurtherNorth());
    addOption(Key.OPTION_SOUTH, CommonText.OPTION_RETURN_SOUTH, new CreviceFallSite());
  }
}
