package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class Unconscious extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DREAM,
    OPTION_AWAKEN
  }
  
  public static enum Img implements ImageKey {
    DREAM,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Img.DREAM);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DREAM);
    addOption(Key.OPTION_EAST, Text.OPTION_AWAKEN, new CreviceFallSite());
  }
}
