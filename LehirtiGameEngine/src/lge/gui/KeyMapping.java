package lge.gui;

import java.io.File;
import java.util.Properties;

import lge.util.FileUtils;
import lge.util.PathFinder;


public final class KeyMapping {
  private static File PROPS_FILE = new File(PathFinder.CONFIG_DIR, KeyMapping.class.getSimpleName()
      + PathFinder.PROPERTIES_FILENAME_SUFFIX);
  
  private static Properties PROPS = null;
  
  static synchronized final int getMappingFor(final String keyName, final int defaultMapping) {
    if (PROPS == null) {
      PROPS = new Properties();
      if (PROPS_FILE.canRead()) {
        FileUtils.readPropsFromFile(PROPS, PROPS_FILE);
      }
    }
    final String keyMapping = (String) PROPS.get(keyName);
    if (keyMapping == null) {
      PROPS.setProperty(keyName, String.valueOf(defaultMapping));
      return defaultMapping;
    } else {
      return Integer.parseInt(keyMapping);
    }
  }
  
  static void store() {
    if (!PROPS_FILE.canWrite()) {
      final File parent = PROPS_FILE.getParentFile();
      if (!parent.exists()) {
        parent.mkdirs();
      }
    }
    PROPS = new Properties();
    for (final Key key : Key.values()) {
      PROPS.setProperty(key.name(), String.valueOf(key.getCode()));
      PROPS.setProperty(key.name() + "mod", String.valueOf(key.getModifiers()));
    }
    FileUtils.writePropsToFile(PROPS, PROPS_FILE,
        "Changes to this file will only take effect after an application restart.");
  }
}
