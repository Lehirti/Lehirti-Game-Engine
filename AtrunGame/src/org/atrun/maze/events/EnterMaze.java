package org.atrun.maze.events;

import org.atrun.images.Background;
import org.atrun.maze.state.MazeState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.StateObject;

public class EnterMaze extends EventNode<NullState> {
  private static final long serialVersionUID = 1L;
  
  public static enum Text implements TextKey {
    MAIN,
    OPTION_ENTER
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Background.ENTER_MAZE);
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
    
    setText(Text.MAIN);
    
    addOption(Key.OPTION_02, Text.OPTION_ENTER, new GoNorth());
  }
}
