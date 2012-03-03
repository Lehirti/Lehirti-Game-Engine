package org.lehirti.state;

import java.io.Serializable;
import java.util.Random;

public class GameState implements Serializable {
  private static final long serialVersionUID = 1L;
  
  public static final Random DIE = new Random();
  
  private final Player player;
  
  public GameState() {
    this.player = new Player();
  }
}
