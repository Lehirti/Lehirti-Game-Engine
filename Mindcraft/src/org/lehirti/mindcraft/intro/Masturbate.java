package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;

public class Masturbate extends EventNode<NullState> {
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(Intro.MASTURBATE);
  }
  
  @Override
  protected void doEvent() {
    setText(Intro.MASTURBATE);
    set(Bool.YOU_ARE_HORNY, false);
    
    addOption(HealerHut.Text.VISIT_HEALER, new HealerHut());
  }
}
