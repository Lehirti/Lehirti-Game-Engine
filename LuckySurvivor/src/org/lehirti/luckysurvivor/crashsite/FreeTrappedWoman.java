package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class FreeTrappedWoman extends EventNode<NullState> {
  public static enum Text implements TextKey {
    FREE_WOMAN_WITH_METAL_STRUT,
    FREE_WOMAN_WITHOUT_METAL_STRUT;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    if (is(CrashSiteBoolInventory.METAL_STRUT)) {
      setText(Text.FREE_WOMAN_WITH_METAL_STRUT);
      set(Plane2_Fuselage.Bool.WOMAN_FREED, true);
    } else {
      setText(Text.FREE_WOMAN_WITHOUT_METAL_STRUT);
    }
    
    addOption(Key.OPTION_EAST, CommonText.OPTION_NEXT, new Plane2_Fuselage());
  }
}
