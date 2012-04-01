package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.TiffaniaWestwood;

public class Night1 extends EventNode {
  public static enum Text implements TextKey {
    ELF,
    OPTION_NEXT
  }
  
  @Override
  protected void doEvent() {
    setText(Text.ELF);
    setImage(TiffaniaWestwood.NIGHT_01);
    
    addOption(Key.NEXT, Text.OPTION_NEXT, new Morning1());
  }
}
