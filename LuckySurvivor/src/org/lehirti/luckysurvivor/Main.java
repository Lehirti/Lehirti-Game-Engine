package org.lehirti.luckysurvivor;

import java.lang.reflect.InvocationTargetException;

import org.lehirti.engine.gui.EngineMain;
import org.lehirti.engine.gui.Notification;
import org.lehirti.engine.state.State;
import org.lehirti.engine.util.ContentUtils;
import org.lehirti.engine.util.ContentUtils.CheckResult;
import org.lehirti.engine.util.PathFinder;
import org.lehirti.luckysurvivor.intro.Startscreen;
import org.lehirti.luckysurvivor.pc.PC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends EngineMain {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  
  /**
   * @param args
   * @throws InvocationTargetException
   * @throws InterruptedException
   */
  public static void main(final String[] args) throws InterruptedException, InvocationTargetException {
    setCurrentEvent(new Startscreen());
    
    new Main().engineMain(args);
  }
  
  @Override
  protected String getGameName() {
    return "[WIP] Lucky Survivor";
  }
  
  @Override
  protected String getSavegameName() {
    return State.get(PC.String.NAME);
  }
  
  @Override
  protected void readContent() {
    for (final C content : C.values()) {
      final CheckResult result = ContentUtils.check(content.name(), content.requiredVersion);
      switch (result) {
      case DEV_ENVIRONMENT:
        LOGGER.info("Development Version");
        //$FALL-THROUGH$
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
        final String msg = "Content " + content.name() + "-" + content.requiredVersion
            + " is not present. You need to download the content pack, if you want this particular content.";
        new Notification(MAIN_WINDOW, msg);
        LOGGER.info(msg);
      }
    }
  }
}
