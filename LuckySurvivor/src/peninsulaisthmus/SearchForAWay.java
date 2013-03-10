package peninsulaisthmus;

import lge.events.AbstractEvent;
import lge.events.TextOnlyEvent;
import lge.events.hooks.OneTimeEventHook;
import lge.gui.Key;

import peninsulaisthmus.MapToPeninsulaIsthmus.Text;

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
