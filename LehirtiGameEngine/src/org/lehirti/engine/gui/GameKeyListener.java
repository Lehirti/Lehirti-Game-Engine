package org.lehirti.engine.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.List;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.InventoryEvent;
import org.lehirti.engine.events.LoadGameScreenEvent;
import org.lehirti.engine.progressgraph.ProgressEvent;
import org.lehirti.engine.progressgraph.TestGraph;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.state.InventoryMap;
import org.lehirti.engine.util.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameKeyListener implements KeyListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(GameKeyListener.class);
  
  private final Main main;
  
  public GameKeyListener(final Main main) {
    this.main = main;
  }
  
  @Override
  public synchronized void keyPressed(final KeyEvent e) {
    LOGGER.info("Key {} {} {} pressed", new Object[] { e.getKeyCode(), e.getModifiers(), e.getKeyChar() });
    // note the synchronized: making sure to only process one key event at a time
    try {
      final Key key = Key.getByCodeAndModifiers(e.getKeyCode(), e.getModifiers());
      
      if (key == null || key == Key.TEXT_INPUT_OPTION_ENTER) {
        // key unrelated to core game
        
        // handle key text input events of non-core-game keys
        if (Main.getCurrentEvent() != null) {
          Main.getCurrentEvent().handleKeyEvent(e, key);
        }
        return;
      }
      
      // let current event handle key event first
      if (Main.getCurrentEvent() != null) {
        if (Main.getCurrentEvent().handleKeyEvent(key)) {
          // current event did use the key, so this key is "used up"
          return;
        }
      }
      
      // let the game engine handle key event
      if (key == Key.IMAGE_EDITOR) {
        editImages();
        return;
      } else if (key == Key.TEXT_EDITOR) {
        editTexts();
        return;
      } else if (key == Key.CYCLE_TEXT_PAGES) {
        Main.getCurrentTextArea().cycleToNextPage();
        return;
      }
      
      // handle key text input events of core-game keys
      if (Main.getCurrentEvent() != null) {
        if (Main.getCurrentEvent().handleKeyEvent(e, key)) {
          return;
        }
      }
      
      if (key == Key.SAVE) {
        this.main.saveGame();
      } else if (key == Key.LOAD) {
        final List<File> allSavegames = PathFinder.getAllSavegames();
        if (allSavegames.isEmpty()) {
          // no savegames found
          new Notification(Main.MAIN_WINDOW, ResourceCache.get(CommonText.NO_SAVEGAME_FOUND), 1500);
        } else {
          final Event<?> oldEvent = Main.getCurrentEvent();
          Main.setCurrentAreas(key);
          Main.setCurrentEvent(new LoadGameScreenEvent(0, allSavegames));
          synchronized (oldEvent) {
            oldEvent.notifyAll();
          }
        }
      } else if (key == Key.SHOW_INVENTORY) {
        final Event<?> oldEvent = Main.getCurrentEvent();
        Main.setCurrentAreas(key);
        Main.setCurrentEvent(new InventoryEvent(InventoryMap.getSelectedItem()));
        synchronized (oldEvent) {
          oldEvent.notifyAll();
        }
      } else if (key == Key.SHOW_PROGRESS) {
        final Event<?> oldEvent = Main.getCurrentEvent();
        Main.setCurrentAreas(key);
        Main.setCurrentEvent(new ProgressEvent(TestGraph.class));
        synchronized (oldEvent) {
          oldEvent.notifyAll();
        }
      } else {
        // key known to the game, but currently not assigned (e.g. one of the option keys)
      }
    } finally {
      e.consume();
    }
  }
  
  @Override
  public void keyReleased(final KeyEvent e) {
  }
  
  @Override
  public synchronized void keyTyped(final KeyEvent e) {
  }
  
  private void editImages() {
    new ImageEditor(Main.getCurrentImageArea().getAllImages(), Main.getCurrentImageArea());
  }
  
  private void editTexts() {
    new TextEditor(Main.getCurrentTextArea(), Main.getCurrentOptionArea(), Main.getCurrentTextArea().getAllTexts());
  }
}
