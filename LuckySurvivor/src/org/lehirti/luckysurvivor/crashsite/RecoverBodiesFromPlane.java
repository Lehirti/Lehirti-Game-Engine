package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class RecoverBodiesFromPlane extends EventNode<NullState> {
  public static enum Text implements TextKey {
    RECOVER_BODIES_WITH_STRECHER,
    RECOVER_BODIES_WITHOUT_STRECHER;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.nullChange();
  }
  
  @Override
  protected void doEvent() {
    if (is(Bool.HAS_STRECHER)) {
      setText(Text.RECOVER_BODIES_WITH_STRECHER);
    } else {
      setText(Text.RECOVER_BODIES_WITHOUT_STRECHER);
    }
    
    addOption(Key.OPTION_EAST, CommonText.OPTION_NEXT, new Plane2_Fuselage());
  }
}