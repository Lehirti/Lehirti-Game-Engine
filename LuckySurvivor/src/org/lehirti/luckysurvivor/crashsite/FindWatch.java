package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.DateTime;
import org.lehirti.engine.state.DateTime.DayOfWeek;

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
