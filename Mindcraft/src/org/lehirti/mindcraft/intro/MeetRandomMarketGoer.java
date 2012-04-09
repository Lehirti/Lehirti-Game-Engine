package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.state.StateObject;

public class MeetRandomMarketGoer extends EventNode<NullState> {
  public static enum MarketGoer implements ImageKey {
    ONE,
    TWO
  }
  
  @Override
  protected void doEvent() {
    final ImageKey key = MarketGoer.values()[StateObject.DIE.nextInt(MarketGoer.values().length)];
    setImage(key);
    setText(key);
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
