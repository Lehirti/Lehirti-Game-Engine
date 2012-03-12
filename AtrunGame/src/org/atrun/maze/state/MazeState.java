package org.atrun.maze.state;

import java.util.Random;

import org.atrun.maze.res.MazeImage;
import org.lehirti.state.IntState;
import org.lehirti.state.StateObject;

public class MazeState extends StateObject {
  private static final long serialVersionUID = 1L;
  
  public static enum Int implements IntState {
    MAZE_LAYOUT(0),
    CURRENT_POSITION_IN_MAZE(0);
    
    private final Long defaultValue;
    
    private Int(final long defaultValue) {
      this.defaultValue = Long.valueOf(defaultValue);
    }
    
    @Override
    public Long defaultValue() {
      return this.defaultValue;
    }
  }
  
  private static long getCurrentPosition() {
    return get(Int.CURRENT_POSITION_IN_MAZE) + get(Int.MAZE_LAYOUT);
  }
  
  public static int goNorth() {
    final long pos = getCurrentPosition();
    final Random mazeDie = new Random(pos);
    final int newPos = mazeDie.nextInt(MazeImage.values().length);
    set(Int.CURRENT_POSITION_IN_MAZE, newPos);
    return newPos;
  }
  
  public static int goEast() {
    final long pos = getCurrentPosition();
    final Random mazeDie = new Random(pos);
    mazeDie.nextInt();
    final int newPos = mazeDie.nextInt(MazeImage.values().length);
    set(Int.CURRENT_POSITION_IN_MAZE, newPos);
    return newPos;
  }
  
  public static int goSouth() {
    final long pos = getCurrentPosition();
    final Random mazeDie = new Random(pos);
    mazeDie.nextInt();
    mazeDie.nextInt();
    final int newPos = mazeDie.nextInt(MazeImage.values().length);
    set(Int.CURRENT_POSITION_IN_MAZE, newPos);
    return newPos;
  }
  
  public static int goWest() {
    final long pos = getCurrentPosition();
    final Random mazeDie = new Random(pos);
    mazeDie.nextInt();
    mazeDie.nextInt();
    mazeDie.nextInt();
    final int newPos = mazeDie.nextInt(MazeImage.values().length);
    set(Int.CURRENT_POSITION_IN_MAZE, newPos);
    return newPos;
  }
}
