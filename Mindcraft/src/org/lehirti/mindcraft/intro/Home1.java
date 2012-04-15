package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.Background;

public class Home1 extends EventNode<Home1.EventState> {
  public static enum Text implements TextKey {
    MAIN,
    SLEEP;
  }
  
  public static enum EventState {
    DAY2;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Background.VILLAGE_HOME_INSIDE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    final EventState eventState = getEventState();
    if (eventState == null) {
      setEventState(EventState.DAY2);
      addOption(Text.SLEEP, new Night2());
    } else {
      switch (getEventState()) {
      case DAY2:
        // TODO?
        setEventState(EventState.DAY2);
        addOption(Text.SLEEP, new Night2());
        break;
      }
    }
  }
}
