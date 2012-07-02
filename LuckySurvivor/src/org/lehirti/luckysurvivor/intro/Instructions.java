package org.lehirti.luckysurvivor.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class Instructions extends EventNode<NullState> {
  public static enum Text implements TextKey {
    INSTRUCTIONS,
    OPTION_GO_BACK;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(IntroImage.INSTRUCTIONS);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.INSTRUCTIONS);
    
    addOption(Key.OPTION_ENTER, Text.OPTION_GO_BACK, new Startscreen());
      }
}
