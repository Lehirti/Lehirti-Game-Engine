package lge.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.List;

import lge.events.Event;
import lge.events.InventoryEvent;
import lge.events.LoadGameScreenEvent;
import lge.progressgraph.AllPGs;
import lge.progressgraph.ProgressEvent;
import lge.res.ResourceCache;
import lge.res.text.CommonText;
import lge.state.InventoryMap;
import lge.util.PathFinder;
import lge.xmlevents.EventEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameKeyListener implements KeyListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(GameKeyListener.class);
  
  private final EngineMain main;
  
  public GameKeyListener(final EngineMain main) {
    this.main = main;
  }
  
  @Override
  public synchronized void keyPressed(final KeyEvent e) {
    LOGGER.debug(
        "Key {} {} {} pressed",
        new Object[] { Integer.valueOf(e.getKeyCode()), Integer.valueOf(e.getModifiers()),
            Character.valueOf(e.getKeyChar()) });
    final StringBuilder sb = new StringBuilder();
    // note the synchronized: making sure to only process one key event at a time
    try {
      final Key key = Key.getByCodeAndModifiers(e.getKeyCode(), e.getModifiers());
      sb.append("Key=");
      sb.append(String.valueOf(key));
      sb.append(";");
      
      // the DONT_PANIC key takes precedence over all other actions, so it's really a don't panic button
      if (key == Key.DONT_PANIC) {
        // try to save, but don't block, if it's not possible ...
        this.main.saveGame(false);
        // ... and exit
        System.exit(0);
      }
      
      if (key == null || key == Key.TEXT_INPUT_OPTION_ENTER) {
        // key unrelated to core game
        sb.append("unrelated to core game;CurrentEvent=");
        
        // handle key text input events of non-core-game keys
        if (EngineMain.getCurrentEvent() != null) {
          EngineMain.getCurrentEvent().handleKeyEvent(e, key);
        }
        sb.append(String.valueOf(EngineMain.getCurrentEvent()));
        
        return;
      }
      
      // let current event handle key event first
      if (EngineMain.getCurrentEvent() != null) {
        sb.append("let current event handle key event first;CurrentEvent=");
        sb.append(String.valueOf(EngineMain.getCurrentEvent()));
        if (EngineMain.getCurrentEvent().handleKeyEvent(key)) {
          // current event did use the key, so this key is "used up"
          sb.append(";current event did use the key");
          return;
        } else {
          sb.append(";current event did NOT use the key");
        }
      }
      
      // let the game engine handle key event
      if (key == Key.IMAGE_EDITOR) {
        editImages();
        return;
      } else if (key == Key.TEXT_EDITOR) {
        editTexts();
        return;
      } else if (key == Key.EVENT_EDITOR) {
        editEvents();
        return;
      } else if (key == Key.CYCLE_TEXT_PAGES) {
        EngineMain.getCurrentTextArea().cycleToNextPage();
        return;
      }
      
      // handle key text input events of core-game keys
      if (EngineMain.getCurrentEvent() != null) {
        sb.append(";handle key text input events of core-game keys");
        if (EngineMain.getCurrentEvent().handleKeyEvent(e, key)) {
          sb.append(";text input event handled");
          return;
        } else {
          sb.append(";text input event NOT handled");
        }
      }
      
      if (key == Key.SAVE) {
        this.main.saveGame(true);
      } else if (key == Key.LOAD) {
        final List<File> allSavegames = PathFinder.getAllSavegames();
        if (allSavegames.isEmpty()) {
          // no save games found
          new Notification(EngineMain.MAIN_WINDOW, ResourceCache.get(CommonText.NO_SAVEGAME_FOUND), 1500);
        } else {
          final Event<?> oldEvent = EngineMain.getCurrentEvent();
          EngineMain.setCurrentAreas(key);
          EngineMain.setCurrentEvent(new LoadGameScreenEvent(0, allSavegames));
          synchronized (oldEvent) {
            oldEvent.notifyAll();
          }
        }
      } else if (key == Key.SHOW_MAIN_SCREEN) {
        final Event<?> oldEvent = EngineMain.getCurrentEvent();
        EngineMain.setCurrentAreas(key);
        synchronized (oldEvent) {
          oldEvent.notifyAll();
        }
      } else if (key == Key.SHOW_INVENTORY) {
        final Event<?> oldEvent = EngineMain.getCurrentEvent();
        EngineMain.setCurrentAreas(key);
        EngineMain.setCurrentEvent(new InventoryEvent(InventoryMap.getSelectedItem()));
        synchronized (oldEvent) {
          oldEvent.notifyAll();
        }
      } else if (key == Key.SHOW_PROGRESS) {
        final Event<?> oldEvent = EngineMain.getCurrentEvent();
        EngineMain.setCurrentAreas(key);
        EngineMain.setCurrentEvent(new ProgressEvent(AllPGs.getCurrent()));
        synchronized (oldEvent) {
          oldEvent.notifyAll();
        }
      } else {
        // key known to the game, but currently not assigned (e.g. one of the option keys)
      }
    } finally {
      LOGGER.debug("Key processed: {}", sb.toString());
      e.consume();
    }
  }
  
  @Override
  public void keyReleased(final KeyEvent e) {
  }
  
  @Override
  public synchronized void keyTyped(final KeyEvent e) {
  }
  
  private static void editEvents() {
    new EventEditor(EngineMain.getCurrentMainEvent().getClass().getPackage().getName(), EngineMain
        .getCurrentMainEvent().getClass().getSimpleName());
  }
  
  private static void editImages() {
    new ImageEditor(EngineMain.getCurrentImageArea().getAllImages(), EngineMain.getCurrentImageArea());
  }
  
  private static void editTexts() {
    new TextEditor(EngineMain.getCurrentTextArea(), EngineMain.getCurrentOptionArea(), EngineMain.getCurrentTextArea()
        .getAllTexts());
  }
}
