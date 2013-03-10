package peninsulaisthmus;

import lge.events.EventNode;
import lge.events.StandardEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImageKey;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.State;

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
