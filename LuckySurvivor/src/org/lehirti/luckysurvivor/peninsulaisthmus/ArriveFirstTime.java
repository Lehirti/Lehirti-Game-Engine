package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class ArriveFirstTime extends EventNode<NullState> {
  public static enum Text implements TextKey {
    TEXT_ARRIVE_FIRST_TIME
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(PeninsulaIsthmus.GORGE);
  }
  
  @Override
  protected void doEvent() {
    set(PeninsulaIsthmusBool.HAS_SEEN_GORGE, true);
    
    setText(Text.TEXT_ARRIVE_FIRST_TIME);
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new MapToPeninsulaIsthmus());
  }
}
