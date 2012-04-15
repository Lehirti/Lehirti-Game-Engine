package org.atrun.maze.events;

import org.atrun.images.Background;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;

public class LeaveMaze extends EventNode<NullState> {
  private static final long serialVersionUID = 1L;
  
  public static enum Text implements TextKey {
    MAIN,
    OPTION_ENTER
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(Background.LEAVE_MAZE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
  }
}
