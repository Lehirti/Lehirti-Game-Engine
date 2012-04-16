package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.Background;
import org.lehirti.mindcraft.images.TiffaniaWestwood;

public class VisitTiffania extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Background.INSIDE_TIFFANIAS_HOME, TiffaniaWestwood.TALKS_TO_YOU);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    // TODO: options
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
