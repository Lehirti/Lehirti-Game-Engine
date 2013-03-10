package crashsite;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

public class RecoverBodiesFromPlane extends EventNode<NullState> {
  public static enum Text implements TextKey {
    RECOVER_BODIES_WITH_STRECHER,
    RECOVER_BODIES_WITHOUT_STRECHER;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    if (is(CrashSiteBoolInventory.STRETCHER)) {
      setText(Text.RECOVER_BODIES_WITH_STRECHER);
      set(CrashSiteBool.DEAD_BODIES_REMOVED_FROM_PLANE, true);
    } else {
      setText(Text.RECOVER_BODIES_WITHOUT_STRECHER);
      set(crashsite.Plane2_Fuselage.Bool.HAS_TRIED_TO_RECOVER_BODIES, true);
    }
    
    addOption(Key.OPTION_EAST, CommonText.OPTION_NEXT, new Plane2_Fuselage());
  }
}
