package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.EventState;
import org.lehirti.luckysurvivor.peninsulaisthmus.CreviceNorth.CreviceState;

public class CreviceNorth extends EventNode<CreviceState> {
  public static enum Text implements TextKey {
    INITIAL_IMPRESSIONS,
    
    OPTION_INVESTIGATE,
    OPTION_EAT_CUCUMBER,
  }
  
  public static enum Img implements ImageKey {
    CREVICE_NORTH
  }
  
  /*
   * here we can't use a simple event count to determine what options to display, since the player may come, leave and
   * come again.
   */
  public static enum CreviceState implements EventState {
    // the initial value is "null"
    INVESTIGATED,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Img.CREVICE_NORTH);
  }
  
  @Override
  protected void doEvent() {
    final CreviceState state = getEventState();
    if (state == null) {
      setText(Text.INITIAL_IMPRESSIONS);
      addOption(Key.OPTION_NORTH, Text.OPTION_INVESTIGATE, new InvestigateCreviceNorth());
      addOption(Key.OPTION_SOUTH, CommonText.OPTION_RETURN_SOUTH, new CreviceFallSite());
    } else {
      switch (state) {
      case INVESTIGATED:
        // TODO
        break;
      }
    }
  }
  
  /*
   * after this event is displayed
   */
  @Override
  public void keyPressed(final Key key) {
    if (getEventState() == null && key == Key.OPTION_NORTH) {
      setEventState(CreviceState.INVESTIGATED);
    }
  }
}
