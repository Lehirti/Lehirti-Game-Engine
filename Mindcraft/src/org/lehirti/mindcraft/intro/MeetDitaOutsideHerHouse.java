package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.EventState;

import org.lehirti.mindcraft.images.Dita;

public class MeetDitaOutsideHerHouse extends EventNode<MeetDitaOutsideHerHouse.State> {
  public static enum Text implements TextKey {
    FIRST_MEETING,
    RECURRING_MEETING
  }
  
  public static enum State implements EventState {
    FIRST_MEETING_DONE
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(Dita.OUTSIDE_HER_HOUSE);
  }
  
  @Override
  protected void doEvent() {
    final State eventState = getEventState();
    if (eventState == null) {
      setText(Text.FIRST_MEETING);
      setEventState(State.FIRST_MEETING_DONE);
      addOption(CommonText.OPTION_LEAVE, new HomeVillage());
    } else {
      switch (getEventState()) {
      case FIRST_MEETING_DONE:
        setText(Text.RECURRING_MEETING);
        addOption(CommonText.OPTION_LEAVE, new HomeVillage());
        break;
      }
    }
  }
}
