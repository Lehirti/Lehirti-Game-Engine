package org.atrun.maze.events;

import org.atrun.maze.res.MazeImage;
import org.atrun.maze.res.MazeText;
import org.lehirti.events.EventNode;
import org.lehirti.gui.Key;
import org.lehirti.res.text.TextKey;

public abstract class MazeEvent extends EventNode {
  private static final long serialVersionUID = 1L;
  
  public static enum Text implements TextKey {
    NORTH,
    EAST,
    SOUTH,
    WEST;
  }
  
  void doEventMain(final int currentPosition) {
    setImage(MazeImage.values()[currentPosition]);
    setText(MazeText.values()[currentPosition]);
    
    addOption(Key.NEXT, Text.NORTH, new GoNorth());
    addOption(Key.NEXT, Text.EAST, new GoEast());
    addOption(Key.NEXT, Text.SOUTH, new GoSouth());
    addOption(Key.NEXT, Text.WEST, new GoWest());
  }
}
