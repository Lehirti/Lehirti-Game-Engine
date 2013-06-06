package lge.gui;

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
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

import javax.swing.JFrame;

import lge.events.Event;
import lge.gui.WindowLocation.WinLoc;
import lge.res.ResourceCache;
import lge.res.images.CommonImage;
import lge.res.images.ImageKey;
import lge.res.images.ImageWrapper;
import lge.res.text.CommonText;
import lge.state.BoolState;
import lge.state.IntState;
import lge.state.State;
import lge.state.StringState;
import lge.util.ClassFinder;
import lge.util.ClassFinder.ClassWorker;
import lge.util.ClassFinder.SuperClass;
import lge.util.ContentUtils;
import lge.util.LogUtils;
import lge.util.PathFinder;
import lge.util.PropertyUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EngineMain {
  static {
    final File logsDir = new File("logs");
    if (!logsDir.exists() && !logsDir.mkdirs()) {
      System.err.println("Could not create logs dir " + logsDir.getAbsolutePath());
    }
    LogUtils.createDefaultLogConfigFileIfMissing();
    System.setProperty("java.util.logging.config.file", "config/logging.properties");
  }
  
  private static final Logger LOGGER = LoggerFactory.getLogger(EngineMain.class);
  
  static {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(final Thread t, final Throwable e) {
        LOGGER.error("Uncaught Exception in Thread " + t.toString(), e);
        e.printStackTrace();
      }
    });
  }
  
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
    
    EngineMain.MAIN_WINDOW = new JFrame(getGameName());
    MAIN_WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    MAIN_WINDOW.getContentPane().setLayout(new GridBagLayout());
    MAIN_WINDOW.addKeyListener(new GameKeyListener(this));
    
    STATS_AREA = new StatsArea(SCREEN_X, SCREEN_Y, 64.0, 6.0);
    
    TEXT_AREAS.put(Key.SHOW_MAIN_SCREEN, new TextArea(SCREEN_X, SCREEN_Y, 16.0, 36.0));
    IMAGE_AREAS.put(Key.SHOW_MAIN_SCREEN, new ImageArea(SCREEN_X, SCREEN_Y, 48.0, 36.0));
    OPTION_AREAS.put(Key.SHOW_MAIN_SCREEN, new OptionArea(SCREEN_X, SCREEN_Y, 64.0, 6.0, 4, 3));
    
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
    
    setCurrentImageArea(IMAGE_AREAS.get(Key.SHOW_MAIN_SCREEN));
    setCurrentTextArea(TEXT_AREAS.get(Key.SHOW_MAIN_SCREEN));
    setCurrentStatsArea(STATS_AREA);
    setCurrentOptionArea(OPTION_AREAS.get(Key.SHOW_MAIN_SCREEN));
    
    MAIN_WINDOW.validate();
    MAIN_WINDOW.repaint();
    
    MAIN_WINDOW.pack();
    try {
      MAIN_WINDOW.createBufferStrategy(2, new BufferCapabilities(new ImageCapabilities(true), new ImageCapabilities(
          true), FlipContents.PRIOR));
    } catch (final AWTException e) {
      LOGGER.debug("Failed to create BufferStrategy", e);
    }
    MAIN_WINDOW.setVisible(true);
    
    final WinLoc winLoc = WindowLocation.getLocationFor(EngineMain.class.getSimpleName());
    if (!winLoc.isMax) {
      MAIN_WINDOW.setLocation(winLoc.x, winLoc.y);
      MAIN_WINDOW.setSize(winLoc.width, winLoc.height);
    } else {
      MAIN_WINDOW.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    MAIN_WINDOW.addWindowListener(new WindowCloseListener(MAIN_WINDOW, EngineMain.class.getSimpleName()));
  }
  
  public static void loadGame(final File sav) {
    try (FileInputStream fis = new FileInputStream(sav); ObjectInputStream ois = new ObjectInputStream(fis)) {
      if (currentEvent == null || !currentEvent.isLoadSavePoint()) {
        LOGGER.warn("Another game cannot be loaded right now"); // better notification for user
        return;
      }
      
      // for load screen preview
      ois.readUTF();
      final int nrImages = ois.readInt();
      for (int i = 0; i < nrImages; i++) {
        ImageKey.IO.read(ois);
      }
      
      State.load(ois);
      STATS_AREA.load(ois);
      int size = ois.readInt();
      for (int i = 0; i < size; i++) {
        final Key key = Key.read(ois);
        IMAGE_AREAS.get(key).load(ois);
      }
      size = ois.readInt();
      for (int i = 0; i < size; i++) {
        final Key key = Key.read(ois);
        TEXT_AREAS.get(key).load(ois);
      }
      size = ois.readInt();
      for (int i = 0; i < size; i++) {
        final Key key = Key.read(ois);
        OPTION_AREAS.get(key).load(ois);
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
    }
  }
  
  protected void saveGame(final boolean notify) {
    final File sav = PathFinder.getNewSaveFile(this.flavor, this.build);
    
    try (FileOutputStream fos = new FileOutputStream(sav); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      if (currentEvent == null || !currentEvent.isLoadSavePoint()) {
        LOGGER.warn("The game cannot be saved right now"); // better notification for user
        if (notify) {
          new Notification(MAIN_WINDOW, ResourceCache.get(CommonText.GAME_NOT_SAVED), 1500);
        }
        return;
      }
      
      // for load screen preview
      oos.writeUTF(getSavegameName());
      final List<ImageWrapper> allImages = IMAGE_AREAS.get(Key.SHOW_MAIN_SCREEN).getAllImages();
      final List<ImageKey> allImageKeys = new LinkedList<>();
      for (final ImageWrapper wrapper : allImages) {
        allImageKeys.add(wrapper.getKey());
      }
      oos.writeInt(allImageKeys.size());
      for (final ImageKey key : allImageKeys) {
        ImageKey.IO.write(key, oos);
      }
      
      State.save(oos);
      STATS_AREA.save(oos);
      oos.writeInt(IMAGE_AREAS.size());
      for (final Map.Entry<Key, ImageArea> entry : IMAGE_AREAS.entrySet()) {
        entry.getKey().write(oos);
        entry.getValue().save(oos);
      }
      oos.writeInt(TEXT_AREAS.size());
      for (final Map.Entry<Key, TextArea> entry : TEXT_AREAS.entrySet()) {
        entry.getKey().write(oos);
        entry.getValue().save(oos);
      }
      oos.writeInt(OPTION_AREAS.size());
      for (final Map.Entry<Key, OptionArea> entry : OPTION_AREAS.entrySet()) {
        entry.getKey().write(oos);
        entry.getValue().save(oos);
      }
      oos.writeObject(currentEvent);
      oos.writeObject(currentInventoryEvent);
      oos.writeObject(currentProgressEvent);
      
      LOGGER.info("Game saved");
      if (notify) {
        new Notification(MAIN_WINDOW, ResourceCache.get(CommonText.GAME_SAVED), 1500);
      }
      
    } catch (final FileNotFoundException e) {
      LOGGER.error("Savegame " + sav.getAbsolutePath() + " not found for saving", e);
    } catch (final IOException e) {
      LOGGER.error("IOException tryting to save to " + sav.getAbsolutePath(), e);
    }
  }
  
  protected abstract String getSavegameName();
  
  public static synchronized Event<?> getCurrentEvent() {
    for (final Map.Entry<Key, ImageArea> entry : IMAGE_AREAS.entrySet()) {
      if (entry.getValue() == currentImageArea) {
        if (entry.getKey() == Key.SHOW_MAIN_SCREEN) {
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
  
  public static synchronized Event<?> getCurrentMainEvent() {
    return currentEvent;
  }
  
  public static synchronized void setCurrentEvent(final Event<?> event) {
    for (final Map.Entry<Key, ImageArea> entry : IMAGE_AREAS.entrySet()) {
      if (entry.getValue() == currentImageArea) {
        if (entry.getKey() == Key.SHOW_MAIN_SCREEN) {
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
    if (!key.isAltScreen) {
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
    final boolean exportDefaults;
    boolean tmpExportDefaults = false;
    boolean editImages = false;
    boolean editTexts = false;
    boolean all = false;
    for (final String arg : args) {
      if (arg.equals("--exportDefaults")) {
        tmpExportDefaults = true;
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
    exportDefaults = tmpExportDefaults;
    
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
            LOGGER.debug("Caching image " + nextImageToPreload.getClass().getName() + "." + nextImageToPreload.name());
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
    ClassFinder.workWithClasses(new ClassWorker() {
      
      @Override
      public void doWork(final List<Class<?>> classes) {
        for (final Class<?> module : classes) {
          LOGGER.debug("Loaded module: {}", module.getName());
        }
      }
      
      @Override
      public SuperClass getSuperClass() {
        return SuperClass.STATIC_INITIALIZER;
      }
      
      @Override
      public String getDescription() {
        return "[Module Loader]";
      }
    });
    
    ClassFinder.workWithClasses(new ClassWorker() {
      
      @Override
      public void doWork(final List<Class<?>> classes) {
        for (final Class<?> state : classes) {
          if (state.isEnum()) {
            if (IntState.class.isAssignableFrom(state)) {
              State.initIntDefaults((Class<IntState>) state);
              if (exportDefaults) {
                PropertyUtils.setDefaultProperties((Class<IntState>) state,
                    PropertyUtils.getDefaultProperties((Class<IntState>) state));
              }
            } else if (StringState.class.isAssignableFrom(state)) {
              State.initStringDefaults((Class<StringState>) state);
              if (exportDefaults) {
                PropertyUtils.setDefaultProperties((Class<StringState>) state,
                    PropertyUtils.getDefaultProperties((Class<StringState>) state));
              }
            } else if (BoolState.class.isAssignableFrom(state)) {
              State.initBoolDefaults((Class<BoolState>) state);
              if (exportDefaults) {
                PropertyUtils.setDefaultProperties((Class<BoolState>) state,
                    PropertyUtils.getDefaultProperties((Class<BoolState>) state));
              }
            }
          }
        }
      }
      
      @Override
      public SuperClass getSuperClass() {
        return SuperClass.ABSTRACT_STATE;
      }
      
      @Override
      public String getDescription() {
        return "[State Initializer]";
      }
    });
    
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