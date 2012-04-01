package org.atrun.maze.state;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.atrun.maze.res.MazeImage;
import org.lehirti.engine.state.IntState;
import org.lehirti.engine.state.ObjState;
import org.lehirti.engine.state.StateObject;

public class MazeState extends StateObject {
  private static final long serialVersionUID = 1L;
  
  private static boolean initialized = false; // TODO game loading
  
  private static int MAZE_SIZE = MazeImage.values().length;
  
  private static final int[][] MAZE_PATHS = new int[MAZE_SIZE][4];
  
  public static enum Obj implements ObjState {
    VISITED_MAZE_LOCATIONS;
    
    @Override
    public Serializable defaultValue() {
      return null;
    }
    
  }
  
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
  
  private static void init() {
    if (initialized) {
      return;
    }
    
    // TODO MAZE_SIZE == 0 case
    
    // always use the same seed to always make the same maze
    final Random mazeDie = new Random(get(Int.MAZE_LAYOUT));
    
    // create one cycle containing all nodes to make sure the entire maze is connected
    final Set<Integer> availableNodes = new HashSet<Integer>();
    for (int i = 0; i < MAZE_SIZE; i++) {
      availableNodes.add(Integer.valueOf(i));
    }
    
    for (int i = 0; i < MAZE_SIZE; i++) {
      for (int j = 0; j < 4; j++) {
        MAZE_PATHS[i][j] = -1;
      }
    }
    
    final int startNode;
    int nextNode;
    int currentNode = startNode = mazeDie.nextInt(MAZE_SIZE);
    availableNodes.remove(Integer.valueOf(currentNode));
    while (!availableNodes.isEmpty()) {
      nextNode = mazeDie.nextInt(MAZE_SIZE);
      while (!availableNodes.remove(Integer.valueOf(nextNode))) {
        nextNode = (nextNode + 1) % MAZE_SIZE;
      }
      final int direction = mazeDie.nextInt(4);
      MAZE_PATHS[currentNode][direction] = nextNode; // going from current node to direction will lead to nextNode
      
      currentNode = nextNode;
    }
    MAZE_PATHS[currentNode][mazeDie.nextInt(4)] = startNode; // close the cycle
    
    // for the rest of paths choose random locations
    for (int i = 0; i < MAZE_SIZE; i++) {
      for (int j = 0; j < 4; j++) {
        if (MAZE_PATHS[i][j] == -1) {
          MAZE_PATHS[i][j] = mazeDie.nextInt(MAZE_SIZE);
        }
      }
    }
    
    initialized = true;
  }
  
  public static int setCurrentPosition(final long currentPosition) {
    final int pos = (int) (Math.abs(currentPosition) % MAZE_SIZE);
    set(Int.CURRENT_POSITION_IN_MAZE, pos);
    boolean[] visitedMazeLocations = (boolean[]) get(Obj.VISITED_MAZE_LOCATIONS);
    if (visitedMazeLocations == null) {
      visitedMazeLocations = new boolean[MAZE_SIZE];
      set(Obj.VISITED_MAZE_LOCATIONS, visitedMazeLocations);
    }
    visitedMazeLocations[pos] = true;
    return pos;
  }
  
  public static boolean isCompletelyExplored() {
    final boolean[] visitedMazeLocations = (boolean[]) get(Obj.VISITED_MAZE_LOCATIONS);
    for (int i = 0; i < MAZE_SIZE; i++) {
      if (visitedMazeLocations[i] == false) {
        return false;
      }
    }
    return true;
  }
  
  private static int getCurrentPosition() {
    return (int) (Math.abs(get(Int.CURRENT_POSITION_IN_MAZE)) % MAZE_SIZE);
  }
  
  private static int go(final int direction) {
    init();
    final int pos = getCurrentPosition();
    final int newPos = MAZE_PATHS[pos][direction];
    return setCurrentPosition(newPos);
  }
  
  public static int goNorth() {
    return go(0);
  }
  
  public static int goEast() {
    return go(1);
  }
  
  public static int goSouth() {
    return go(2);
  }
  
  public static int goWest() {
    return go(3);
  }
}
