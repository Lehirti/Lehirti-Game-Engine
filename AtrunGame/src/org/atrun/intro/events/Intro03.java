package org.atrun.intro.events;

import org.lehirti.events.EventNode;
import org.lehirti.gui.Key;
import org.lehirti.res.text.TextKey;

public class Intro03 extends EventNode {
  public static enum Text implements TextKey {
    MAIN,
    OPTION_PREVIOUS
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    addOption(Key.PREVIOUS, Text.OPTION_PREVIOUS, new Intro02());
  }
}
