package org.atrun.intro.events;

import org.atrun.intro.IntroImage;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.TextKey;

public class Intro02 extends EventNode {
  public static enum Text implements TextKey {
    MAIN,
    OPTION_NEXT,
    OPTION_PREVIOUS
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    setImage(IntroImage.INTRO_02);
    
    addOption(Key.PREVIOUS, Text.OPTION_PREVIOUS, new Intro01());
    addOption(Key.NEXT, Text.OPTION_NEXT, new Intro03());
  }
}
