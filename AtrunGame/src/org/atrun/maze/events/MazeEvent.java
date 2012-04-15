package org.atrun.maze.events;

import org.atrun.maze.res.MazeImage;
import org.atrun.maze.res.MazeText;
import org.atrun.maze.state.MazeState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.TextKey;

public abstract class MazeEvent extends EventNode<NullState> {
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
      addOption(Key.OPTION_02, Text.LEAVE, new LeaveMaze());
    } else {
      addOption(Key.OPTION_03, Text.NORTH, new GoNorth());
      addOption(Key.OPTION_04, Text.EAST, new GoEast());
      addOption(Key.OPTION_05, Text.SOUTH, new GoSouth());
      addOption(Key.OPTION_06, Text.WEST, new GoWest());
    }
  }
}
