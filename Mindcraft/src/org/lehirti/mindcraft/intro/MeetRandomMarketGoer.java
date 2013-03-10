package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImageKey;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.State;

public class MeetRandomMarketGoer extends EventNode<NullState> {
  public static enum MarketGoer implements ImageKey, TextKey {
    ONE,
    TWO
  }
  
  private int choice = -1;
  
  @Override
  protected synchronized ImgChange updateImageArea() {
    if (this.choice == -1) {
      this.choice = State.DIE.nextInt(MarketGoer.values().length);
    }
    return ImgChange.setFG(MarketGoer.values()[this.choice]);
  }
  
  @Override
  protected void doEvent() {
    final TextKey key = MarketGoer.values()[this.choice];
    setText(key);
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
