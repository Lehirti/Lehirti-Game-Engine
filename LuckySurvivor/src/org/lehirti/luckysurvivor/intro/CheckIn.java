package org.lehirti.luckysurvivor.intro;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.pc.PC;

public class CheckIn extends EventNode<NullState> {
  public static enum Text implements TextKey {
    CHECKIN_DESCRIPTION,
    MY_NAME_IS;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(IntroImage.CHECKIN_BACKGROUND, IntroImage.PROFESSOR_OAK, IntroImage.INFO_DESK);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.CHECKIN_DESCRIPTION);
    
    setTextInputOption(Text.MY_NAME_IS, "Generic Player 9001", new Airport());
  }
  
  @Override
  protected boolean handleTextInput(final String finalTextInput) {
    set(PC.String.NAME, finalTextInput);
    return true;
  }
}
