package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.AbstractEvent;
import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.events.hooks.OneTimeEventHook;
import org.lehirti.engine.gui.Key;
import org.lehirti.luckysurvivor.peninsulaisthmus.MapToPeninsulaIsthmus.Text;

/* 
 * this is just a TextOnlyEvent, but it needs to be named, in order to be properly hook-able. you could "TextOnlyEvent"
 * as well, but it's used all over the place and all of its occurrences would get hooked. not what we want.
 */
public class SearchForAWay extends TextOnlyEvent {
  static {
    AbstractEvent.registerHook(SearchForAWay.class, new OneTimeEventHook(FindTree.class, Double.valueOf(10.0)));
  }
  
  public SearchForAWay() {
    super(Key.OPTION_WEST, Text.SEARCH_FOR_A_WAY, new MapToPeninsulaIsthmus());
  }
}
