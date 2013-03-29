package main;

import intro.Startscreen;

import java.lang.reflect.InvocationTargetException;

import lge.gui.EngineMain;
import lge.gui.Notification;
import lge.state.State;
import lge.util.ContentUtils;
import lge.util.ContentUtils.CheckResult;
import lge.util.PathFinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pc.PC;

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
