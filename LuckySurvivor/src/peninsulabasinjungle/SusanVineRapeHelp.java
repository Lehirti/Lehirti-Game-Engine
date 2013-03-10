package peninsulabasinjungle;

import lge.events.TextOnlyEvent;
import lge.gui.Key;

public class SusanVineRapeHelp extends TextOnlyEvent {
  public SusanVineRapeHelp() {
    super(Key.OPTION_ENTER, SusanVineRape.Text.TEXT_TRY_TO_HELP, new SusanVineRape());
  }
}
