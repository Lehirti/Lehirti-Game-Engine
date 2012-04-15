package org.atrun.intro.events;

import org.atrun.intro.state.LocationTracker.Int;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class Intro04 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN,
    OPTION_NEXT
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.nullChange();
  }
  
  @Override
  protected void doEvent() {
    change(Int.BEEN_TO_INTRO_04, 1);
    
    setText(Text.MAIN);
    
    addOption(Key.OPTION_02, Text.OPTION_NEXT, new Intro01());
  }
  
}
