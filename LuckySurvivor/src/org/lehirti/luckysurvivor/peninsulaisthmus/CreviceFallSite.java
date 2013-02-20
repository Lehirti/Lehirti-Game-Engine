package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.State;

public class CreviceFallSite extends EventNode<NullState> {
  public static enum Text implements TextKey {
    INITIAL_IMPRESSIONS,
    OPTION_INVESTIGATE,
    
    INVESTIGATION_REPORT,
  }
  
  public static enum Img implements ImageKey {
    CREVICE_FALL_SITE,
    CREVICE_SOUTH,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Img.CREVICE_FALL_SITE);
  }
  
  @Override
  protected void doEvent() {
    if (State.getEventCount(CreviceFallSite.class) == 1) {
      setText(Text.INITIAL_IMPRESSIONS);
      addOption(Key.OPTION_EAST, Text.OPTION_INVESTIGATE, new CreviceFallSite());
    } else {
      setText(Text.INVESTIGATION_REPORT);
      addOption(Key.OPTION_NORTH, CommonText.OPTION_GO_NORTH, new CreviceNorth());
      // option south will show one standard event and then return back here
      addOption(Key.OPTION_SOUTH, CommonText.OPTION_GO_SOUTH, new StandardEvent(Key.OPTION_SOUTH, Img.CREVICE_SOUTH,
          CommonText.OPTION_GO_BACK, new CreviceFallSite()));
    }
  }
}
