package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class FallIntoCrevice extends EventNode<NullState> {
  public static enum Text implements TextKey {
    FALL,
    OPTION_GOOD_BY_CRUEL_WORLD
  }
  
  public static enum Img implements ImageKey {
    FALL,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Img.FALL);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.FALL);
    addOption(Key.OPTION_EAST, Text.OPTION_GOOD_BY_CRUEL_WORLD, new Unconscious());
  }
}
