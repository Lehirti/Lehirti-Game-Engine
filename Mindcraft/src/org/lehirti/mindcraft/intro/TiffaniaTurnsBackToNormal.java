package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.StandardEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

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
    
    addOption(Key.OPTION_NORTH, Text.ASK_FOR_TITJOB, new StandardEvent(Key.OPTION_NORTH, TiffaniaWestwood.TITJOB,
        Text.RECEIVE_TITJOB, new TiffaniaTurnsBackToNormal()));
    if (is(Bool.TIFFANIA_HAS_TOLD_YOU_ALL_SHE_KNOWS)) {
      addOption(Text.GO_BACK_TO_VILLAGE, new VisitTiffania());
    } else {
      addOption(Text.ASK_HER_TO_EXPLAIN, new TiffaniaTellsWhatSheKnows());
    }
  }
}
