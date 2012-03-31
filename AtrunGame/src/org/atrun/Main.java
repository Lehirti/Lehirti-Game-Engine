package org.atrun;

import java.lang.reflect.InvocationTargetException;

import org.atrun.maze.events.EnterMaze;
import org.lehirti.util.ContentUtils;
import org.lehirti.util.PathFinder;
import org.lehirti.util.ContentUtils.CheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends org.lehirti.Main {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  
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
  
  @Override
  protected void readContent() {
    for (final C content : C.values()) {
      final CheckResult result = ContentUtils.check(content.name(), content.requiredVersion);
      switch (result) {
      case DEV_ENVIRONMENT:
        LOGGER.info("Development Version");
        // fall through
      case OK:
        LOGGER.info("Content " + content.name() + "-" + content.requiredVersion + " is present.");
        content.available = true;
        PathFinder.registerContentDir(content.name());
        break;
      case NEEDS_UPDATE:
        LOGGER.info("Content " + content.name() + "-" + content.requiredVersion + " needs updating.");
        ContentUtils.rebuild(content.name(), content.requiredVersion);
        PathFinder.registerContentDir(content.name());
        break;
      case MISSING:
        LOGGER.info("Content " + content.name() + "-" + content.requiredVersion
            + " is not present. You need to download the content pack, if you want this particular content.");
      }
    }
  }
}
