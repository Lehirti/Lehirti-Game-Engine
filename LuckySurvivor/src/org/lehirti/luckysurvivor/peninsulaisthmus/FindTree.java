package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.TextOnlyEvent;
import org.lehirti.engine.gui.Key;
import org.lehirti.luckysurvivor.peninsulaisthmus.MapToPeninsulaIsthmus.Text;

public class FindTree extends TextOnlyEvent {
  public FindTree() {
    super(Key.OPTION_WEST, Text.FIND_TREE, new MapToPeninsulaIsthmus());
  }
}
