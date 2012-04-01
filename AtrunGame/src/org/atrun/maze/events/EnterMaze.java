package org.atrun.maze.events;

import org.atrun.images.Background;
import org.atrun.maze.state.MazeState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.StateObject;

public class EnterMaze extends EventNode {
  private static final long serialVersionUID = 1L;
  
  public static enum Text implements TextKey {
    MAIN,
    OPTION_ENTER
  }
  
  @Override
  protected void doEvent() {
    long mazeLayout = get(MazeState.Int.MAZE_LAYOUT);
    
    // initialize maze; once per game
    if (mazeLayout == 0L) {
      mazeLayout = StateObject.DIE.nextLong();
      set(MazeState.Int.MAZE_LAYOUT, mazeLayout);
    }
    
    // enter maze at random point
    MazeState.setCurrentPosition(StateObject.DIE.nextLong());
    
    setBackgroundImage(Background.ENTER_MAZE);
    setImage(null);
    setText(Text.MAIN);
    
    addOption(Key.NEXT, Text.OPTION_ENTER, new GoNorth());
  }
  
}
