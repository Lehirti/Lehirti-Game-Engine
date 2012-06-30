package org.lehirti.luckysurvivor.peninsulabasinjungle;

import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.gui.Key;

public class SusanVineRapeHelp extends TextOnlyEvent {
  public SusanVineRapeHelp() {
    super(Key.OPTION_ENTER, SusanVineRape.Text.TEXT_TRY_TO_HELP, new SusanVineRape());
  }
}
