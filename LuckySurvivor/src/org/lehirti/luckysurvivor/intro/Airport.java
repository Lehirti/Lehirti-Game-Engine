package org.lehirti.luckysurvivor.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.crashsite.Plane1_YourSeat;

public class Airport extends EventNode<NullState> {
  public static enum Text implements TextKey {
    AIRPORT_DESCRIPTION,
    OPTION_ENTER_BUS,
    OPTION_SKIP_INTRO;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(IntroImage.AIRPORT);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.AIRPORT_DESCRIPTION);
    
    addOption(Key.OPTION_ENTER, Text.OPTION_ENTER_BUS, new Bus());
    addOption(Key.OPTION_LEAVE, Text.OPTION_SKIP_INTRO, new Plane1_YourSeat());
  }
}
