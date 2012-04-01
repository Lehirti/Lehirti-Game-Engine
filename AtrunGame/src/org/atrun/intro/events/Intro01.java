package org.atrun.intro.events;

import org.atrun.intro.IntroImage;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.TextKey;

public class Intro01 extends EventNode {
  public static enum Text implements TextKey {
    MAIN,
    OPTION_NEXT
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    setBackgroundImage(IntroImage.INTRO_01);
    
    addOption(Key.NEXT, Text.OPTION_NEXT, new Intro02());
  }
}
