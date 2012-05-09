package org.lehirti.luckysurvivor;

import java.lang.reflect.InvocationTargetException;

import org.lehirti.engine.util.ContentUtils;
import org.lehirti.engine.util.PathFinder;
import org.lehirti.engine.util.ContentUtils.CheckResult;
import org.lehirti.luckysurvivor.intro.Airport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends org.lehirti.engine.Main {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  
  /**
   * @param args
   * @throws InvocationTargetException
   * @throws InterruptedException
   */
  public static void main(final String[] args) throws InterruptedException, InvocationTargetException {
    org.lehirti.engine.Main.currentEvent = new Airport();
    
    new Main().engineMain(args);
  }
  
  @Override
  protected String getGameName() {
    return "[WIP] Lucky Survivor";
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
