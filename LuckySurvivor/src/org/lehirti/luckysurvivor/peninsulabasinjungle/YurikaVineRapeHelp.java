package org.lehirti.luckysurvivor.peninsulabasinjungle;

import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.gui.Key;

public class YurikaVineRapeHelp extends TextOnlyEvent {
  public YurikaVineRapeHelp() {
    super(Key.OPTION_ENTER, YurikaVineRape.Text.TEXT_TRY_TO_HELP, new YurikaVineRape());
  }
}
