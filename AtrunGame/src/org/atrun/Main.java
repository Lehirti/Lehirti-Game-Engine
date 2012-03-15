package org.atrun;

import java.lang.reflect.InvocationTargetException;

import org.atrun.maze.events.EnterMaze;

public class Main extends org.lehirti.Main {
  
  /**
   * @param args
   * @throws InvocationTargetException
   * @throws InterruptedException
   */
  public static void main(final String[] args) throws InterruptedException, InvocationTargetException {
    org.lehirti.Main.currentEvent = new EnterMaze();
    
    new Main().engineMain(args);
  }
  
  @Override
  protected String getGameName() {
    return "Game";
  }
  
}
