package org.atrun.maze.events;

import org.atrun.maze.res.MazeImage;
import org.atrun.maze.res.MazeText;
import org.atrun.maze.state.MazeState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
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
  
  ImgChange updateImageArea(final int mazePositionForThisEvent) {
    return ImgChange.setFG(MazeImage.values()[mazePositionForThisEvent]);
  }
  
  void doEventMain(final int mazePositionForThisEvent) {
    setText(MazeText.values()[mazePositionForThisEvent]);
    
    if (MazeState.isCompletelyExplored()) {
      addOption(Key.OPTION_LEAVE, Text.LEAVE, new LeaveMaze());
    } else {
      addOption(Key.OPTION_NORTH, Text.NORTH, new GoNorth());
      addOption(Key.OPTION_ENTER, Text.EAST, new GoEast());
      addOption(Key.OPTION_A, Text.SOUTH, new GoSouth());
      addOption(Key.OPTION_WEST, Text.WEST, new GoWest());
    }
  }
}
