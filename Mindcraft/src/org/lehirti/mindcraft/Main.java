package org.lehirti.mindcraft;

import java.lang.reflect.InvocationTargetException;

import lge.util.ContentUtils;
import lge.util.PathFinder;
import lge.util.ContentUtils.CheckResult;

import org.lehirti.mindcraft.intro.Night1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends lge.gui.EngineMain {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  
  /**
   * @param args
   * @throws InvocationTargetException
   * @throws InterruptedException
   */
  public static void main(final String[] args) throws InterruptedException, InvocationTargetException {
    setCurrentEvent(new Night1());
    
    new Main().engineMain(args);
  }
  
  @Override
  protected String getGameName() {
    return "Mindcraft";
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
        ContentUtils.rebuild(content.name(), Integer.valueOf(content.requiredVersion));
        PathFinder.registerContentDir(content.name());
        break;
      case MISSING:
        LOGGER.info("Content " + content.name() + "-" + content.requiredVersion
            + " is not present. You need to download the content pack, if you want this particular content.");
      }
    }
  }
  
  @Override
  protected String getSavegameName() {
    return "TODO"; // TODO
  }
}
