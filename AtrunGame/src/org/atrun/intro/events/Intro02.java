package org.atrun.intro.events;

import org.atrun.intro.IntroImage;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class Intro02 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN,
    OPTION_NEXT,
    OPTION_PREVIOUS
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(IntroImage.INTRO_02);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    addOption(Key.OPTION_01, Text.OPTION_PREVIOUS, new Intro01());
    addOption(Key.OPTION_02, Text.OPTION_NEXT, new Intro03());
  }
}
