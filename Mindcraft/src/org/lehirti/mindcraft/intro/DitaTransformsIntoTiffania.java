package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

import org.lehirti.mindcraft.images.TiffaniaWestwood;

public class DitaTransformsIntoTiffania extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(null, TiffaniaWestwood.MIND_CONTROLLED_IN_THE_WOODS);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    addOption(CommonText.OPTION_NEXT, new TiffaniaTurnsBackToNormal());
  }
}
