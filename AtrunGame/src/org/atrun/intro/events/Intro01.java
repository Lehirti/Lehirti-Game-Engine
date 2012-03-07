package org.atrun.intro.events;

import org.atrun.intro.IntroImage;
import org.lehirti.events.EventNode;
import org.lehirti.gui.Key;
import org.lehirti.res.text.TextKey;

public class Intro01 extends EventNode {
  public static enum Text implements TextKey {
    MAIN,
    OPTION_NEXT
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    setBackgroundImage(IntroImage.INTRO_01);
    setImage(IntroImage.INTRO_02);
    
    addOption(Key.NEXT, Text.OPTION_NEXT, new Intro02());
  }
}
