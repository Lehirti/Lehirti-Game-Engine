package peninsulaisthmus;

import lge.events.TextOnlyEvent;
import lge.gui.Key;

import peninsulaisthmus.MapToPeninsulaIsthmus.Text;

public class FindTree extends TextOnlyEvent {
  public FindTree() {
    super(Key.OPTION_WEST, Text.FIND_TREE, new MapToPeninsulaIsthmus());
  }
}
