package org.lehirti.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.InventoryEvent;
import org.lehirti.engine.gui.ImageEditor;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.gui.TextEditor;
import org.lehirti.engine.state.InventoryMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameKeyListener implements KeyListener {
  private static final Logger LOGGER = LoggerFactory.getLogger(GameKeyListener.class);
  
  @Override
  public void keyPressed(final KeyEvent e) {
  }
  
  @Override
  public void keyReleased(final KeyEvent e) {
  }
  
  @Override
  public synchronized void keyTyped(final KeyEvent e) {
    LOGGER.info("Key {} typed", e.getKeyChar());
    // note the synchronized: making sure to only process one key event at a time
    try {
      final Key key = Key.getByChar(e.getKeyChar());
      
      if (key == null) {
        // key unrelated to the game pressed
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
      if (key == Key.CTRL_I) {
        editImages();
      } else if (key == Key.CTRL_T) {
        editTexts();
      } else if (key == Key.CTRL_S) {
        Main.saveGame();
      } else if (key == Key.CTRL_L) {
        Main.loadGame();
      } else if (key == Key.SHOW_INVENTORY) {
        final Event<?> oldEvent = Main.getCurrentEvent();
        Main.setCurrentImageArea(Main.INVENTORY_IMAGE_AREA);
        Main.setCurrentTextArea(Main.INVENTORY_TEXT_AREA);
        Main.setCurrentOptionArea(Main.INVENTORY_OPTION_AREA);
        Main.setCurrentEvent(new InventoryEvent(InventoryMap.getSelectedItem()));
        synchronized (oldEvent) {
          oldEvent.notifyAll();
        }
      } else if (key == Key.CYCLE_TEXT_PAGES) {
        Main.getCurrentTextArea().cycleToNextPage();
      } else {
        // key known to the game, but currently not assigned (e.g. one of the option keys)
      }
    } finally {
      e.consume();
    }
  }
  
  private void editImages() {
    new ImageEditor(Main.getCurrentImageArea().getAllImages(), Main.getCurrentImageArea());
  }
  
  private void editTexts() {
    new TextEditor(Main.getCurrentTextArea(), Main.getCurrentOptionArea(), Main.getCurrentTextArea().getAllTexts());
  }
}
