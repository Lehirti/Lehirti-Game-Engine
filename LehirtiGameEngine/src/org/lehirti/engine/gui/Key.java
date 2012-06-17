package org.lehirti.engine.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public enum Key {
  OPTION_Q('q', 0, 0),
  OPTION_LEAVE('w', 1, 0),
  OPTION_NORTH('e', 2, 0),
  OPTION_ENTER('r', 3, 0),
  OPTION_A('a', 0, 1),
  OPTION_WEST('s', 1, 1),
  OPTION_SOUTH('d', 2, 1),
  OPTION_EAST('f', 3, 1),
  OPTION_Z('z', 0, 2),
  OPTION_X('x', 1, 2),
  OPTION_C('c', 2, 2),
  OPTION_V('v', 3, 2),
  
  // 
  SHOW_MAIN('1'),
  SHOW_INVENTORY('2'),
  
  CYCLE_TEXT_PAGES(' '),
  
  // image and text editors
  CTRL_I((char) 0x09),
  CTRL_T((char) 0x14),
  
  // save and load
  CTRL_S((char) 0x13),
  CTRL_L((char) 0x0c);
  
  private static final List<Key> OPTION_KEYS = new ArrayList<Key>(values().length);
  
  static {
    KeyMapping.store();
    for (final Key key : values()) {
      if (key.isOptionKey) {
        OPTION_KEYS.add(key);
      }
    }
  }
  
  public final char mapping;
  public final boolean isOptionKey;
  public final int col;
  public final int row;
  
  private Key(final char defaultMapping, final int col, final int row) {
    this.mapping = KeyMapping.getMappingFor(name(), defaultMapping);
    this.isOptionKey = true;
    this.col = col;
    this.row = row;
  }
  
  private Key(final char defaultMapping) {
    this.mapping = KeyMapping.getMappingFor(name(), defaultMapping);
    this.isOptionKey = false;
    this.col = -1;
    this.row = -1;
  }
  
  public static Key getByChar(final char mapping) {
    for (final Key key : values()) {
      if (key.mapping == mapping) {
        return key;
      }
    }
    return null;
  }
  
  public static List<Key> getOptionKeys() {
    final List<Key> optionKeys = new LinkedList<Key>(OPTION_KEYS);
    return optionKeys;
  }
}
