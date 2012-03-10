package org.atrun.intro.events;

import org.atrun.intro.state.LocationTracker.Int;
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
    change(Int.BEEN_TO_INTRO_04, 1);
    
    setText(Text.MAIN);
    
    addOption(Key.NEXT, Text.OPTION_NEXT, new Intro01());
  }
  
}
