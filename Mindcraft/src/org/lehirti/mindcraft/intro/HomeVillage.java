package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.Background;
import org.lehirti.mindcraft.regionhometocapitol.HomeVillageToCapitol;

public class HomeVillage extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN,
    OPTION_GO_HOME,
    OPTION_VISIT_HEALER,
    OPTION_VISIT_MARKET,
    OPTION_VISIT_NEXT_DOOR_NEIGHBOUR,
    OPTION_VISIT_BATHHOUSE,
    OPTION_VISIT_TIFFANIA,
    OPTION_LEAVE_FOR_THE_CAPITOL;
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
    if (is(Bool.TIFFANIA_HAS_TOLD_YOU_ALL_SHE_KNOWS)) {
      addOption(Text.OPTION_VISIT_TIFFANIA, new VisitTiffania());
      addOption(Text.OPTION_LEAVE_FOR_THE_CAPITOL, new HomeVillageToCapitol());
    } else {
      addOption(Text.OPTION_VISIT_NEXT_DOOR_NEIGHBOUR, new Neighbour());
    }
    addOption(Text.OPTION_VISIT_BATHHOUSE, new Bathhouse());
  }
}
