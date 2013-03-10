package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

import org.lehirti.mindcraft.images.Dita;

public class FuckDitaInWoods extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(null, Dita.FUCKED_IN_WOODS);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    addOption(CommonText.OPTION_NEXT, new DitaTransformsIntoTiffania());
  }
}
