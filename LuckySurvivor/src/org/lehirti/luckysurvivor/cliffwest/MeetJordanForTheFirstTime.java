package org.lehirti.luckysurvivor.cliffwest;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class MeetJordanForTheFirstTime extends EventNode<NullState> {
  public static enum Text implements TextKey {
    TEXT_TALK_TO_JORDAN_FOR_THE_FIRST_TIME
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(CliffWest.JORDAN);
  }
  
  @Override
  protected void doEvent() {
    set(CliffWestBool.HAS_MET_JORDAN, true);
    
    setText(Text.TEXT_TALK_TO_JORDAN_FOR_THE_FIRST_TIME);
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new MapToCliffWest());
  }
}
