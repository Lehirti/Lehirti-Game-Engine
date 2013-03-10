package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

import org.lehirti.mindcraft.images.TiffaniaWestwood;

public class Night2 extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DREAM2
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(null, TiffaniaWestwood.NIGHT_02);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DREAM2);
    
    addOption(CommonText.OPTION_NEXT, new Morning2());
  }
}
