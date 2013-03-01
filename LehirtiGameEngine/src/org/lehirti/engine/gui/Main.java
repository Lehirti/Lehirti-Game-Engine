package org.lehirti.engine.gui;

import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.BufferCapabilities.FlipContents;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ImageCapabilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import javax.swing.JFrame;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.gui.WindowLocation.WinLoc;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.images.CommonImage;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImageWrapper;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.state.BoolState;
import org.lehirti.engine.state.IntState;
import org.lehirti.engine.state.State;
import org.lehirti.engine.state.StaticInitializer;
import org.lehirti.engine.state.StringState;
import org.lehirti.engine.util.ClassFinder;
import org.lehirti.engine.util.ContentUtils;
import org.lehirti.engine.util.LogUtils;
import org.lehirti.engine.util.PathFinder;
import org.lehirti.engine.util.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Main {
  static {
    final File logsDir = new File("logs");
    if (!logsDir.exists() && !logsDir.mkdirs()) {
      System.err.println("Could not create logs dir " + logsDir.getAbsolutePath());
    }
    LogUtils.createDefaultLogConfigFileIfMissing();
    System.setProperty("java.util.logging.config.file", "config/logging.properties");
  }
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  
  public static JFrame MAIN_WINDOW;
  
  private static final double SCREEN_X = 64.0;
  private static final double SCREEN_Y = 48.0;
  
  public static StatsArea STATS_AREA;
  
  private static Map<Key, TextArea> TEXT_AREAS = new LinkedHashMap<>();
  private static Map<Key, ImageArea> IMAGE_AREAS = new LinkedHashMap<>();
  private static Map<Key, OptionArea> OPTION_AREAS = new LinkedHashMap<>();
  
  private static volatile ImageArea currentImageArea = null;
  private static volatile TextArea currentTextArea = null;
  private static volatile StatsArea currentStatsArea = null;
  private static volatile OptionArea currentOptionsArea = null;
  
  public static boolean IS_DEVELOPMENT_VERSION = false;
  
  private static volatile Event<?> currentEvent = null;
  private static volatile Event<?> currentInventoryEvent = null;
  private static volatile Event<?> currentProgressEvent = null;
  
  private String flavor = "flavor";
  
  private String build = "build";
  
  private void createAndShowGUI() {
    
    Main.MAIN_WINDOW = new JFrame(getGameName());
    MAIN_WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    MAIN_WINDOW.getContentPane().setLayout(new GridBagLayout());
    MAIN_WINDOW.addKeyListener(new GameKeyListener(this));
    
    STATS_AREA = new StatsArea(SCREEN_X, SCREEN_Y, 64.0, 6.0);
    
    TEXT_AREAS.put(null, new TextArea(SCREEN_X, SCREEN_Y, 16.0, 36.0));
    IMAGE_AREAS.put(null, new ImageArea(SCREEN_X, SCREEN_Y, 48.0, 36.0));
    OPTION_AREAS.put(null, new OptionArea(SCREEN_X, SCREEN_Y, 64.0, 6.0, 4, 3));
    
    TEXT_AREAS.put(Key.SHOW_INVENTORY, new TextArea(SCREEN_X, SCREEN_Y, 16.0, 36.0));
    IMAGE_AREAS.put(Key.SHOW_INVENTORY, new ImageArea(SCREEN_X, SCREEN_Y, 48.0, 36.0));
    OPTION_AREAS.put(Key.SHOW_INVENTORY, new OptionArea(SCREEN_X, SCREEN_Y, 64.0, 6.0, 4, 3));
    
    TEXT_AREAS.put(Key.SHOW_PROGRESS, new TextArea(SCREEN_X, SCREEN_Y, 16.0, 36.0));
    IMAGE_AREAS.put(Key.SHOW_PROGRESS, new ProgressImageArea(SCREEN_X, SCREEN_Y, 48.0, 36.0));
    OPTION_AREAS.put(Key.SHOW_PROGRESS, new OptionArea(SCREEN_X, SCREEN_Y, 64.0, 6.0, 4, 3));
    
    TEXT_AREAS.put(Key.LOAD, new TextArea(SCREEN_X, SCREEN_Y, 16.0, 36.0));
    IMAGE_AREAS.put(Key.LOAD, new ImageArea(SCREEN_X, SCREEN_Y, 48.0, 36.0));
    OPTION_AREAS.put(Key.LOAD, new OptionArea(SCREEN_X, SCREEN_Y, 64.0, 6.0, 4, 3));
    
    IMAGE_AREAS.get(Key.SHOW_INVENTORY).setBackgroundImage(CommonImage.INVENTORY_BACKGROUND);
    IMAGE_AREAS.get(Key.SHOW_PROGRESS).setBackgroundImage(CommonImage.PROGRESS_BACKGROUND);
    
    GridBagConstraints c;
    
    for (final ImageArea imageArea : IMAGE_AREAS.values()) {
      c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = 0;
      c.gridwidth = 1;
      c.gridheight = 1;
      MAIN_WINDOW.getContentPane().add(imageArea, c);
    }
    
    for (final TextArea textArea : TEXT_AREAS.values()) {
      c = new GridBagConstraints();
      c.gridx = 1;
      c.gridy = 0;
      c.gridwidth = 1;
      c.gridheight = 1;
      MAIN_WINDOW.getContentPane().add(textArea, c);
    }
    
    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 1;
    c.gridwidth = 2;
    c.gridheight = 1;
    MAIN_WINDOW.getContentPane().add(STATS_AREA, c);
    
    for (final OptionArea optionArea : OPTION_AREAS.values()) {
      c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = 2;
      c.gridwidth = 2;
      c.gridheight = 1;
      MAIN_WINDOW.getContentPane().add(optionArea, c);
    }
    
    setCurrentImageArea(IMAGE_AREAS.get(null));
    setCurrentTextArea(TEXT_AREAS.get(null));
    setCurrentStatsArea(STATS_AREA);
    setCurrentOptionArea(OPTION_AREAS.get(null));
    
    MAIN_WINDOW.validate();
    MAIN_WINDOW.repaint();
    
    MAIN_WINDOW.pack();
    try {
      MAIN_WINDOW.createBufferStrategy(2, new BufferCapabilities(new ImageCapabilities(true), new ImageCapabilities(
          true), FlipContents.PRIOR));
    } catch (final AWTException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    MAIN_WINDOW.setVisible(true);
    
    final WinLoc winLoc = WindowLocation.getLocationFor("Main");
    if (!winLoc.isMax) {
      MAIN_WINDOW.setLocation(winLoc.x, winLoc.y);
      MAIN_WINDOW.setSize(winLoc.width, winLoc.height);
    } else {
      MAIN_WINDOW.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    MAIN_WINDOW.addWindowListener(new WindowCloseListener(MAIN_WINDOW, "Main"));
  }
  
  public static void loadGame(final File sav) {
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    try {
      if (currentEvent == null || !currentEvent.isLoadSavePoint()) {
        LOGGER.warn("Another game cannot be loaded right now"); // better notification for user
        return;
      }
      fis = new FileInputStream(sav);
      ois = new ObjectInputStream(fis);
      
      // for load screen preview
      ois.readUTF();
      final int nrImages = ois.readInt();
      for (int i = 0; i < nrImages; i++) {
        ImageKey.IO.read(ois);
      }
      
      State.load(ois);
      STATS_AREA.readExternal(ois);
      int size = ois.readInt();
      for (int i = 0; i < size; i++) {
        final Key key = (Key) ois.readObject();
        IMAGE_AREAS.get(key).readExternal(ois);
      }
      size = ois.readInt();
      for (int i = 0; i < size; i++) {
        final Key key = (Key) ois.readObject();
        TEXT_AREAS.get(key).readExternal(ois);
      }
      size = ois.readInt();
      for (int i = 0; i < size; i++) {
        final Key key = (Key) ois.readObject();
        OPTION_AREAS.get(key).readExternal(ois);
      }
      final Event<?> oldEvent = currentEvent;
      currentEvent = (Event<?>) ois.readObject();
      currentInventoryEvent = (Event<?>) ois.readObject();
      currentProgressEvent = (Event<?>) ois.readObject();
      synchronized (oldEvent) {
        oldEvent.newEventHasBeenLoaded();
        oldEvent.notifyAll();
      }
      
      LOGGER.info("Game loaded");
      
      MAIN_WINDOW.repaint();
      
    } catch (final FileNotFoundException e) {
      LOGGER.error("Savegame " + sav.getAbsolutePath() + " not found for loading", e);
    } catch (final IOException e) {
      LOGGER.error("IOException tryting to load from " + sav.getAbsolutePath(), e);
    } catch (final ClassNotFoundException e) {
      LOGGER.error("Savegame " + sav.getAbsolutePath() + " incompatible with current program version.", e);
    } finally {
      if (ois != null) {
        try {
          ois.close();
        } catch (final IOException e) {
          LOGGER.warn("Failed to close object input stream for " + sav.getAbsolutePath(), e);
        }
      }
      if (fis != null) {
        try {
          fis.close();
        } catch (final IOException e) {
          LOGGER.warn("Failed to close file input stream for " + sav.getAbsolutePath(), e);
        }
      }
    }
  }
  
  protected void saveGame() {
    final File sav = PathFinder.getNewSaveFile(this.flavor, this.build);
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;
    try {
      if (currentEvent == null || !currentEvent.isLoadSavePoint()) {
        LOGGER.warn("The game cannot be saved right now"); // better notification for user
        return;
      }
      
      fos = new FileOutputStream(sav);
      oos = new ObjectOutputStream(fos);
      
      // for load screen preview
      oos.writeUTF(getSavegameName());
      final List<ImageWrapper> allImages = IMAGE_AREAS.get(null).getAllImages();
      final List<ImageKey> allImageKeys = new LinkedList<>();
      for (final ImageWrapper wrapper : allImages) {
        allImageKeys.add(wrapper.getKey());
      }
      oos.writeInt(allImageKeys.size());
      for (final ImageKey key : allImageKeys) {
        ImageKey.IO.write(key, oos);
      }
      
      State.save(oos);
      STATS_AREA.writeExternal(oos);
      oos.writeInt(IMAGE_AREAS.size());
      for (final Map.Entry<Key, ImageArea> entry : IMAGE_AREAS.entrySet()) {
        oos.writeObject(entry.getKey());
        entry.getValue().writeExternal(oos);
      }
      oos.writeInt(TEXT_AREAS.size());
      for (final Map.Entry<Key, TextArea> entry : TEXT_AREAS.entrySet()) {
        oos.writeObject(entry.getKey());
        entry.getValue().writeExternal(oos);
      }
      oos.writeInt(OPTION_AREAS.size());
      for (final Map.Entry<Key, OptionArea> entry : OPTION_AREAS.entrySet()) {
        oos.writeObject(entry.getKey());
        entry.getValue().writeExternal(oos);
      }
      oos.writeObject(currentEvent);
      oos.writeObject(currentInventoryEvent);
      oos.writeObject(currentProgressEvent);
      
      LOGGER.info("Game saved");
      
      new Notification(MAIN_WINDOW, ResourceCache.get(CommonText.GAME_SAVED), 1500);
      
    } catch (final FileNotFoundException e) {
      LOGGER.error("Savegame " + sav.getAbsolutePath() + " not found for saving", e);
    } catch (final IOException e) {
      LOGGER.error("IOException tryting to save to " + sav.getAbsolutePath(), e);
    } finally {
      if (oos != null) {
        try {
          oos.close();
        } catch (final IOException e) {
          LOGGER.warn("Failed to close object output stream for " + sav.getAbsolutePath(), e);
        }
      }
      if (fos != null) {
        try {
          fos.close();
        } catch (final IOException e) {
          LOGGER.warn("Failed to close file output stream for " + sav.getAbsolutePath(), e);
        }
      }
    }
  }
  
  protected abstract String getSavegameName();
  
  public static synchronized Event<?> getCurrentEvent() {
    for (final Map.Entry<Key, ImageArea> entry : IMAGE_AREAS.entrySet()) {
      if (entry.getValue() == currentImageArea) {
        if (entry.getKey() == null) {
          return currentEvent;
        }
        if (entry.getKey() == Key.SHOW_INVENTORY) {
          return currentInventoryEvent;
        }
        return currentProgressEvent;
      }
    }
    return currentEvent;
  }
  
  public static synchronized void setCurrentEvent(final Event<?> event) {
    for (final Map.Entry<Key, ImageArea> entry : IMAGE_AREAS.entrySet()) {
      if (entry.getValue() == currentImageArea) {
        if (entry.getKey() == null) {
          currentEvent = event;
          return;
        }
        if (entry.getKey() == Key.SHOW_INVENTORY) {
          currentInventoryEvent = event;
          return;
        }
        currentProgressEvent = event;
        return;
      }
    }
    currentEvent = event;
    return;
  }
  
  public static TextArea getCurrentTextArea() {
    return currentTextArea;
  }
  
  public static ImageArea getCurrentImageArea() {
    return currentImageArea;
  }
  
  public static StatsArea getCurrentStatsArea() {
    return currentStatsArea;
  }
  
  public static OptionArea getCurrentOptionArea() {
    return currentOptionsArea;
  }
  
  public static void setCurrentAreas(final Key key) {
    if (key != null && !key.isAltScreen) {
      return; // TODO error message
    }
    setCurrentImageArea(IMAGE_AREAS.get(key));
    setCurrentTextArea(TEXT_AREAS.get(key));
    setCurrentOptionArea(OPTION_AREAS.get(key));
  }
  
  private static void setCurrentTextArea(final TextArea textArea) {
    for (final TextArea txtArea : TEXT_AREAS.values()) {
      if (txtArea != textArea) {
        txtArea.setVisible(false);
      }
    }
    
    textArea.setVisible(true);
    textArea.requestFocusInWindow();
    currentTextArea = textArea;
  }
  
  private static void setCurrentImageArea(final ImageArea imageArea) {
    for (final ImageArea imgArea : IMAGE_AREAS.values()) {
      if (imgArea != imageArea) {
        imgArea.setVisible(false);
      }
    }
    
    imageArea.setVisible(true);
    currentImageArea = imageArea;
  }
  
  private static void setCurrentStatsArea(final StatsArea statsArea) {
    currentStatsArea = statsArea;
  }
  
  private static void setCurrentOptionArea(final OptionArea optionArea) {
    for (final OptionArea optArea : OPTION_AREAS.values()) {
      if (optArea != optionArea) {
        optArea.setVisible(false);
      }
    }
    
    optionArea.setVisible(true);
    currentOptionsArea = optionArea;
  }
  
  protected void engineMain(final String[] args) throws InterruptedException, InvocationTargetException {
    boolean exportDefaults = false;
    boolean editImages = false;
    boolean editTexts = false;
    boolean all = false;
    for (final String arg : args) {
      if (arg.equals("--exportDefaults")) {
        exportDefaults = true;
      }
      if (arg.equals("--editImages")) {
        editImages = true;
      }
      if (arg.equals("--editTexts")) {
        editTexts = true;
      }
      if (arg.equals("--all")) {
        all = true;
      }
    }
    
    readVersion();
    
    readContent();
    
    // start background daemon thread to preload next images
    final Thread backgroundImageLoader = new Thread(new Runnable() {
      
      @Override
      public void run() {
        final BlockingQueue<ImageKey> imagesToPreload = ResourceCache.getImagesToPreload();
        try {
          while (true) {
            final ImageKey nextImageToPreload = imagesToPreload.take();
            LOGGER.info("Caching image " + nextImageToPreload.getClass().getName() + "." + nextImageToPreload.name());
            ResourceCache.cache(nextImageToPreload);
          }
        } catch (final InterruptedException e) {
          LOGGER.warn("Thread " + Thread.currentThread().getName() + " has been interrupted and is terminating.", e);
          return;
        }
      }
    }, "BackgroundImageLoader");
    backgroundImageLoader.setDaemon(true);
    backgroundImageLoader.start();
    
    /*
     * load all modules
     */
    final Vector<Class<?>> modules = new ClassFinder().findSubclasses(StaticInitializer.class.getName());
    for (final Class<?> module : modules) {
      LOGGER.debug("Loaded module: {}", module.getName());
    }
    
    Vector<Class<?>> states = new ClassFinder().findSubclasses(IntState.class.getName());
    for (final Class<?> intState : states) {
      if (intState.isEnum()) {
        State.initIntDefaults((Class<IntState>) intState);
        if (exportDefaults) {
          PropertyUtils.setDefaultProperties((Class<IntState>) intState,
              PropertyUtils.getDefaultProperties((Class<IntState>) intState));
        }
      }
    }
    
    states = new ClassFinder().findSubclasses(StringState.class.getName());
    for (final Class<?> stringState : states) {
      if (stringState.isEnum()) {
        State.initStringDefaults((Class<StringState>) stringState);
        if (exportDefaults) {
          PropertyUtils.setDefaultProperties((Class<StringState>) stringState,
              PropertyUtils.getDefaultProperties((Class<StringState>) stringState));
        }
      }
    }
    
    states = new ClassFinder().findSubclasses(BoolState.class.getName());
    for (final Class<?> boolState : states) {
      if (boolState.isEnum()) {
        State.initBoolDefaults((Class<BoolState>) boolState);
        if (exportDefaults) {
          PropertyUtils.setDefaultProperties((Class<BoolState>) boolState,
              PropertyUtils.getDefaultProperties((Class<BoolState>) boolState));
        }
      }
    }
    
    javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
    
    LOGGER.info("{} started.", getGameName());
    
    if (editImages || editTexts) {
      if (editImages) {
        new ImageEditor(ContentUtils.getImageWrappers(!all), getCurrentImageArea());
      }
      if (editTexts) {
        new TextEditor(getCurrentTextArea(), getCurrentOptionArea(), ContentUtils.getTextWrappers(!all));
      }
    } else {
      while (true) {
        getCurrentEvent().execute();
      }
    }
  }
  
  private void readVersion() {
    final InputStream versionInputStream = ClassLoader.getSystemResourceAsStream(PathFinder
        .getLocationOfVerionsFileOnClasspath());
    final Properties versionProps = new Properties();
    try {
      versionProps.load(versionInputStream);
    } catch (final Exception e) {
      LOGGER.info(getGameName() + " Development Version");
      IS_DEVELOPMENT_VERSION = true;
      return;
    }
    this.flavor = versionProps.getProperty("flavor");
    this.build = versionProps.getProperty("build");
    
    LOGGER.info(getGameName() + " " + this.flavor + " Build " + this.build + " " + versionProps.getProperty("date"));
  }
  
  abstract protected void readContent();
  
  abstract protected String getGameName();
  
  public static Font getScaledFont() {
    return currentTextArea.getScaledFont();
  }
}