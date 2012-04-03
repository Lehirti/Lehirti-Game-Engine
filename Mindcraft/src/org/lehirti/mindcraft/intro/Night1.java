package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.TiffaniaWestwood;

public class Night1 extends EventNode {
  public static enum Text implements TextKey {
    ELF
  }
  
  @Override
  protected void doEvent() {
    setText(Text.ELF);
    setImage(TiffaniaWestwood.NIGHT_01);
    
    addOption(CommonText.OPTION_NEXT, new Morning1());
  }
}
