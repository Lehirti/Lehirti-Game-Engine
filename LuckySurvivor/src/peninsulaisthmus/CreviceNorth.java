package peninsulaisthmus;

import lge.events.EventNode;
import lge.gui.Key;
import lge.res.images.ImageKey;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.EventState;
import lge.state.State;

import peninsulaisthmus.CreviceNorth.CreviceState;

public class CreviceNorth extends EventNode<CreviceState> {
  public static enum Text implements TextKey {
    INITIAL_IMPRESSIONS,
    EJACULATION_FROM_EFFECTS_OF_CUCUMBER,
    THIS_IS_WHERE_YOU_FOUND_THE_CUCUMBER,
    OPTION_SEARCH_FOR_CUCUMBERS,
    
    OPTION_INVESTIGATE,
  }
  
  public static enum Img implements ImageKey {
    CREVICE_NORTH,
    EJACULATION_FROM_EFFECTS_OF_CUCUMBER
  }
  
  /*
   * here we can't use a simple event count to determine what options to display, since the player may come, leave and
   * come again.
   */
  public static enum CreviceState implements EventState {
    // the initial value is "null"
    INVESTIGATED,
    CUCUMBER_EATEN,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    if (getEventState() == CreviceState.INVESTIGATED) {
      return ImgChange.setBGAndFG(Img.CREVICE_NORTH, Img.EJACULATION_FROM_EFFECTS_OF_CUCUMBER);
    } else {
      return ImgChange.setBG(Img.CREVICE_NORTH);
    }
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
        setText(Text.EJACULATION_FROM_EFFECTS_OF_CUCUMBER);
        setEventState(CreviceState.CUCUMBER_EATEN);
        break;
      case CUCUMBER_EATEN:
        setText(Text.THIS_IS_WHERE_YOU_FOUND_THE_CUCUMBER);
        break;
      }
      if (State.getEventCount(FoundCucumber.class) == 0) {
        addOption(Key.OPTION_EAST, Text.OPTION_SEARCH_FOR_CUCUMBERS, new FoundCucumber());
      }
      addOption(Key.OPTION_NORTH, CommonText.OPTION_GO_NORTH, new CreviceFurtherNorth());
      addOption(Key.OPTION_SOUTH, CommonText.OPTION_RETURN_SOUTH, new CreviceFallSite());
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
