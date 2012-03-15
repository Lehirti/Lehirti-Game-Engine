package org.atrun.maze.events;

import org.atrun.maze.res.MazeImage;
import org.atrun.maze.res.MazeText;
import org.atrun.maze.state.MazeState;
import org.lehirti.events.EventNode;
import org.lehirti.gui.Key;
import org.lehirti.res.text.TextKey;

public abstract class MazeEvent extends EventNode {
  private static final long serialVersionUID = 1L;
  
  public static enum Text implements TextKey {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    LEAVE;
  }
  
  void doEventMain(final int currentPosition) {
    setImage(MazeImage.values()[currentPosition]);
    setText(MazeText.values()[currentPosition]);
    
    if (MazeState.isCompletelyExplored()) {
      addOption(Key.NEXT, Text.LEAVE, new LeaveMaze());
    } else {
      addOption(Key.NORTH, Text.NORTH, new GoNorth());
      addOption(Key.EAST, Text.EAST, new GoEast());
      addOption(Key.SOUTH, Text.SOUTH, new GoSouth());
      addOption(Key.WEST, Text.WEST, new GoWest());
    }
  }
}
