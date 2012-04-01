package org.lehirti.engine.gui;

import java.io.File;
import java.util.Properties;

import org.lehirti.engine.util.FileUtils;

public final class KeyMapping {
  private static File PROPS_FILE = new File("config/KeyMapping.properties");
  
  private static Properties PROPS = null;
  
  static synchronized final char getMappingFor(final String keyName, final char defaultMapping) {
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
      return keyMapping.charAt(0);
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
      PROPS.setProperty(key.name(), String.valueOf(key.mapping));
    }
    FileUtils.writePropsToFile(PROPS, PROPS_FILE,
        "Changes to this file will only take effect after an application restart.");
  }
}
