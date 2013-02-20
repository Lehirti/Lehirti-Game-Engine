package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.State;

public final class GoEastWithoutTree extends EventNode<NullState> {
  public static enum Text implements TextKey {
    CLIMB_AND_FALL,
    OPTION_TRY_GRABBING
  }
  
  public static enum Img implements ImageKey {
    CLIMB_AND_FALL,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    /*
     * the occurrence of every event (that is not an "unnamed" TextOnly/StandardEvent) gets counted automatically. so
     * the following if-statement means the same as "if this is the first time the "GoEastWithoutTree" event occurs".
     * the event count is updated after the updateImageArea() and doEvent() methods are called.
     */
    if (State.getEventCount(GoEastWithoutTree.class) == 0) {
      return ImgChange.setBGAndFG(Img.CLIMB_AND_FALL);
    } else {
      return ImgChange.nullChange();
    }
  }
  
  @Override
  protected void doEvent() {
    if (State.getEventCount(GoEastWithoutTree.class) == 1) {
      setText(Text.CLIMB_AND_FALL);
      addOption(Key.OPTION_EAST, Text.OPTION_TRY_GRABBING, new FallIntoCrevice());
    } else {
      setText(MapToPeninsulaIsthmus.Text.TEXT_NO_WAY_ACROSS);
      addOption(Key.OPTION_EAST, CommonText.OPTION_NEXT, new MapToPeninsulaIsthmus());
    }
  }
}
