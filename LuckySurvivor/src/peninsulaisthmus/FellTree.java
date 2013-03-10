package peninsulaisthmus;

import lge.events.SetFlagTextOnlyEvent;
import lge.gui.Key;
import map.Map;

import peninsulaisthmus.MapToPeninsulaIsthmus.Text;

public class FellTree extends SetFlagTextOnlyEvent {
  public FellTree() {
    super(Map.Path.PENINSULA_ISTHMUS___ISLAND_ENTRY, Key.OPTION_WEST, Text.FELL_TREE, new MapToPeninsulaIsthmus());
  }
}
