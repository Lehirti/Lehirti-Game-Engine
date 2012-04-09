package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.Dita;

public class FuckDitaInWoods extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN
  }
  
  @Override
  protected void doEvent() {
    setBackgroundImage(null);
    setText(Text.MAIN);
    setImage(Dita.FUCKED_IN_WOODS);
    
    addOption(CommonText.OPTION_NEXT, new DitaTransformsIntoTiffania());
  }
}
