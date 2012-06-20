package org.lehirti.luckysurvivor.peninsulabasinjungle;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.StateObject;

public class YurikaVineRape extends EventNode<NullState> {
  
  public static enum Text implements TextKey {
    DESCRIPTION,
    OPTION_LOOK_AT_FACE,
    OPTION_LOOK_AT_BREASTS,
    OPTION_LOOK_AT_PUSSY,
    OPTION_LOOK_AT_ASS,
    OPTION_TRY_TO_HELP,
    TEXT_TRY_TO_HELP;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(PeninsulaBasinJungle.PENINSULA_BASIN_JUNGLE, PeninsulaBasinJungle.YURIKA_VINE_RAPED);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    addOption(Key.OPTION_NORTH, Text.OPTION_LOOK_AT_FACE, new YurikaVineRapeFace());
    addOption(Key.OPTION_WEST, Text.OPTION_LOOK_AT_BREASTS, new YurikaVineRapeBreasts());
    addOption(Key.OPTION_SOUTH, Text.OPTION_LOOK_AT_PUSSY, new YurikaVineRapePussy());
    addOption(Key.OPTION_EAST, Text.OPTION_LOOK_AT_ASS, new YurikaVineRapeAss());
    
    if (StateObject.getEventCount(YurikaVineRapeHelp.class) == 0) {
      addOption(Key.OPTION_ENTER, Text.OPTION_TRY_TO_HELP, new YurikaVineRapeHelp());
    }
  }
}
