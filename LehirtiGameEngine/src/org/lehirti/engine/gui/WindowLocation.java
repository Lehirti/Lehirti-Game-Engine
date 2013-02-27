package org.lehirti.engine.gui;

import java.io.File;
import java.util.Properties;

import org.lehirti.engine.util.FileUtils;
import org.lehirti.engine.util.PathFinder;

public final class WindowLocation {
  private static File PROPS_FILE = new File(PathFinder.CONFIG_DIR, WindowLocation.class.getSimpleName()
      + PathFinder.PROPERTIES_FILENAME_SUFFIX);
  
  private static final String MAX = "_max";
  private static final String X = "_x";
  private static final String Y = "_y";
  private static final String WIDTH = "_width";
  private static final String HEIGHT = "_height";
  
  private static Properties PROPS = null;
  
  public static final class WinLoc {
    public boolean isMax;
    public int x;
    public int y;
    public int width;
    public int height;
  }
  
  public static synchronized final WinLoc getLocationFor(final String windowName) {
    if (PROPS == null) {
      PROPS = new Properties();
      if (PROPS_FILE.canRead()) {
        FileUtils.readPropsFromFile(PROPS, PROPS_FILE);
      }
    }
    final WinLoc loc = new WinLoc();
    final String max = (String) PROPS.get(windowName + MAX);
    final String x = (String) PROPS.get(windowName + X);
    final String y = (String) PROPS.get(windowName + Y);
    final String width = (String) PROPS.get(windowName + WIDTH);
    final String height = (String) PROPS.get(windowName + HEIGHT);
    if (x != null && y != null && width != null && height != null) {
      loc.isMax = Boolean.parseBoolean(max);
      loc.x = Integer.parseInt(x);
      loc.y = Integer.parseInt(y);
      loc.width = Integer.parseInt(width);
      loc.height = Integer.parseInt(height);
    } else {
      loc.isMax = true;
    }
    return loc;
  }
  
  public static synchronized void store(final String windowName, final boolean isMax, final int x, final int y,
      final int width, final int height) {
    if (!PROPS_FILE.canWrite()) {
      final File parent = PROPS_FILE.getParentFile();
      if (!parent.exists()) {
        parent.mkdirs();
      }
    }
    if (PROPS == null) {
      PROPS = new Properties();
      if (PROPS_FILE.canRead()) {
        FileUtils.readPropsFromFile(PROPS, PROPS_FILE);
      }
    }
    PROPS.setProperty(windowName + MAX, String.valueOf(isMax));
    PROPS.setProperty(windowName + X, String.valueOf(x));
    PROPS.setProperty(windowName + Y, String.valueOf(y));
    PROPS.setProperty(windowName + WIDTH, String.valueOf(width));
    PROPS.setProperty(windowName + HEIGHT, String.valueOf(height));
    
    FileUtils.writePropsToFile(PROPS, PROPS_FILE,
        "Changes to this file will only take effect after an application restart.");
  }
}
