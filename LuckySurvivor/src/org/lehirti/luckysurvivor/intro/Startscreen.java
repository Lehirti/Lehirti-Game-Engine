package org.lehirti.luckysurvivor.intro;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;



public class Startscreen extends EventNode<NullState> {
  public static enum Text implements TextKey {
    STARTSCREEN_DESCRIPTION,
    OPTION_START,
    OPTION_INSTRUCTIONS,
    OPTION_THANKS;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(IntroImage.STARTSCREEN);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.STARTSCREEN_DESCRIPTION);
    
    addOption(Key.OPTION_ENTER, Text.OPTION_START, new Airport());
    addOption(Key.OPTION_LEAVE, Text.OPTION_INSTRUCTIONS, new Instructions());
    addOption(Key.OPTION_WEST, Text.OPTION_THANKS, new Thanks());
  }
}