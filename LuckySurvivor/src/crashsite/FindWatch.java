package crashsite;

import lge.events.EventNode;
import lge.events.StandardEvent;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.DateTime;
import lge.state.DateTime.DayOfWeek;

public class FindWatch extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(CrashSiteBoolInventory.WATCH);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    set(CrashSiteBoolInventory.WATCH, true);
    DateTime.init(DayOfWeek.WEDNESDAY, 1, 21, 1, 29);
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new StandardEvent(Key.OPTION_LEAVE, null,
        Plane1_YourSeat.Text.LEAVE_YOUR_SEAT, new Plane1_Fuselage()));
  }
}
