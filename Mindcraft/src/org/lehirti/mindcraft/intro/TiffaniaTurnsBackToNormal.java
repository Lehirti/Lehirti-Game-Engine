package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.Background;
import org.lehirti.mindcraft.images.TiffaniaWestwood;

public class TiffaniaTurnsBackToNormal extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN,
    ASK_HER_TO_EXPLAIN,
    ASK_FOR_TITJOB,
    RECEIVE_TITJOB,
    GO_BACK_TO_VILLAGE
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Background.VILLAGE_WOODS, TiffaniaWestwood.ON_HER_KNEES);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    addOption(Text.ASK_FOR_TITJOB, new StandardEvent(TiffaniaWestwood.TITJOB, Text.RECEIVE_TITJOB,
        new TiffaniaTurnsBackToNormal()));
    if (is(Bool.TIFFANIA_HAS_TOLD_YOU_ALL_SHE_KNOWS)) {
      addOption(Text.GO_BACK_TO_VILLAGE, new VisitTiffania());
    } else {
      addOption(Text.ASK_HER_TO_EXPLAIN, new TiffaniaTellsWhatSheKnows());
    }
  }
}
