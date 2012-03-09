package org.atrun.intro.events;

import org.lehirti.events.EventNode;
import org.lehirti.gui.Key;
import org.lehirti.res.text.TextKey;

public class Intro04 extends EventNode {
  public static enum Text implements TextKey {
    MAIN,
    OPTION_NEXT
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    addOption(Key.NEXT, Text.OPTION_NEXT, new Intro01());
  }
  
}
