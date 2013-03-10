package cliffwest;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

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
    setText(Text.TEXT_TALK_TO_JORDAN_FOR_THE_FIRST_TIME);
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new MapToCliffWest());
  }
}
