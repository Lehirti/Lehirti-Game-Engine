package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class BathhouseThreeGirls extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(Intro.THREE_GIRLS_IN_BATHHOUSE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
