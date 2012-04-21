package org.atrun.maze.events;

import org.atrun.maze.state.MazeState;
import org.lehirti.engine.res.images.ImgChange;

public class GoWest extends MazeEvent {
  private static final long serialVersionUID = 1L;
  
  @Override
  protected ImgChange updateImageArea() {
    return updateImageArea(MazeState.lookWest());
  }
  
  @Override
  protected void doEvent() {
    doEventMain(MazeState.goWest());
  }
}
