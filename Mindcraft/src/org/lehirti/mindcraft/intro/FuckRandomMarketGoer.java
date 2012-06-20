package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.State;

public class FuckRandomMarketGoer extends EventNode<NullState> {
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
  protected synchronized void doEvent() {
    final TextKey key = MarketGoer.values()[this.choice];
    setText(key);
    set(Bool.YOU_ARE_HORNY, false);
    addOption(CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
