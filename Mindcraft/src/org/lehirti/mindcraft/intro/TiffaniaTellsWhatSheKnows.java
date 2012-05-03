package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.TiffaniaWestwood;

public class TiffaniaTellsWhatSheKnows extends EventNode<NullState> {
  public static enum Text implements TextKey {
    EXPLANATION,
    OPTION_HEAD_BACK_TO_HOME_VILLAGE;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(TiffaniaWestwood.TALKS_TO_YOU_IN_THE_WOODS);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.EXPLANATION);
    
    set(Bool.TIFFANIA_HAS_TOLD_YOU_ALL_SHE_KNOWS, true);
    
    addOption(Text.OPTION_HEAD_BACK_TO_HOME_VILLAGE, new HomeVillage());
  }
}
