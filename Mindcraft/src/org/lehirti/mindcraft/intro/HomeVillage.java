package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.Background;

public class HomeVillage extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN,
    OPTION_GO_HOME,
    OPTION_VISIT_HEALER,
    OPTION_VISIT_MARKET,
    OPTION_VISIT_NEXT_DOOR_NEIGHBOUR,
    OPTION_VISIT_BATHHOUSE;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Background.HOME_VILLAGE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    addOption(Text.OPTION_GO_HOME, new Home1());
    addOption(Text.OPTION_VISIT_MARKET, new Market());
    addOption(Text.OPTION_VISIT_NEXT_DOOR_NEIGHBOUR, new Neighbour());
    addOption(Text.OPTION_VISIT_BATHHOUSE, new Bathhouse());
  }
}
