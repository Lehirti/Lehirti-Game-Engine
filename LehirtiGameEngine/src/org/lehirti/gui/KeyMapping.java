package org.lehirti.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public final class KeyMapping {
  private static File PROPS_FILE = new File("config/KeyMapping.properties");
  
  private static Properties PROPS = null;
  
  static synchronized final char getMappingFor(final String keyName, final char defaultMapping) {
    if (PROPS == null) {
      PROPS = new Properties();
      if (PROPS_FILE.canRead()) {
        try {
          PROPS.load(new FileInputStream(PROPS_FILE));
        } catch (final FileNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (final IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
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
    try {
      PROPS = new Properties();
      for (final Key key : Key.values()) {
        PROPS.setProperty(key.name(), String.valueOf(key.mapping));
      }
      PROPS.store(new FileOutputStream(PROPS_FILE),
          "Changes to this file will only take effect after an application restart.");
    } catch (final FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
