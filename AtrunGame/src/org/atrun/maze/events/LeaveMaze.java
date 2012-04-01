package org.atrun.maze.events;

import org.atrun.images.Background;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.res.text.TextKey;

public class LeaveMaze extends EventNode {
  private static final long serialVersionUID = 1L;
  
  public static enum Text implements TextKey {
    MAIN,
    OPTION_ENTER
  }
  
  @Override
  protected void doEvent() {
    setBackgroundImage(Background.LEAVE_MAZE);
    setImage(null);
    setText(Text.MAIN);
  }
  
}
