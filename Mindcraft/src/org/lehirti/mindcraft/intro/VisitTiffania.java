package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.Background;
import org.lehirti.mindcraft.images.TiffaniaWestwood;

public class VisitTiffania extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN,
    OPTION_FUCK_HER,
    OPTION_RECEIVE_TITJOB
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Background.INSIDE_TIFFANIAS_HOME, TiffaniaWestwood.TALKS_TO_YOU);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    addOption(Text.OPTION_FUCK_HER, new StandardEvent(TiffaniaWestwood.FUCK_HER_INHOUSE, CommonText.OPTION_NEXT,
        new VisitTiffania()));
    addOption(Text.OPTION_RECEIVE_TITJOB, new StandardEvent(TiffaniaWestwood.TITJOB_INHOUSE, CommonText.OPTION_NEXT,
        new VisitTiffania()));
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
