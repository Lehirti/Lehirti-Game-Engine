package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.text.TextKey;

public class DitaTransformsIntoTiffania extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN
  }
  
  @Override
  protected void doEvent() {
    setBackgroundImage(null);
    setImage(null);
    setText(Text.MAIN);
    
    // TODO
  }
}
