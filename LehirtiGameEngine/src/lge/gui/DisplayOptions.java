package lge.gui;

import java.io.File;
import java.util.Properties;

import lge.util.FileUtils;


public final class DisplayOptions {
  private static File PROPS_FILE = new File("config/" + DisplayOptions.class.getSimpleName() + ".properties");
  
  private static Properties PROPS = null;
  
  static synchronized final String getDisplayOptionFor(final String keyName, final String defaultDisplayOption) {
    if (PROPS == null) {
      PROPS = new Properties();
      if (PROPS_FILE.canRead()) {
        FileUtils.readPropsFromFile(PROPS, PROPS_FILE);
      }
    }
    final String displayOption = (String) PROPS.get(keyName);
    if (displayOption == null) {
      PROPS.setProperty(keyName, defaultDisplayOption);
      FileUtils.writePropsToFile(PROPS, PROPS_FILE,
          "Changes to this file will only take effect after an application restart.");
      return defaultDisplayOption;
    } else {
      return displayOption;
    }
  }
}
