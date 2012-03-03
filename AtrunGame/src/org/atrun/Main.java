package org.atrun;

import java.lang.reflect.InvocationTargetException;

import org.atrun.modules.intro.Intro01;

public class Main extends org.lehirti.Main {
  
  /**
   * @param args
   * @throws InvocationTargetException
   * @throws InterruptedException
   */
  public static void main(final String[] args) throws InterruptedException, InvocationTargetException {
    org.lehirti.Main.nextEvent = new Intro01();
    
    engineMain(args);
  }
  
}
