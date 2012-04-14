package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.text.CommonText;

public class Masturbate2 extends EventNode<NullState> {
  
  @Override
  protected void doEvent() {
    setText(Masturbate.Text.TEXT);
    setImage(Intro.MASTURBATE);
    set(Bool.YOU_ARE_HORNY, false);
    
    addOption(CommonText.LEAVE_HOUSE, new HomeVillage());
  }
}
