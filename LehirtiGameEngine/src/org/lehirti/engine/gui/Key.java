package org.lehirti.engine.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public enum Key {
  PREVIOUS('r', true),
  NEXT('f', true),
  
  NORTH('w', true),
  EAST('d', true),
  SOUTH('s', true),
  WEST('a', true),
  
  // image and text editors
  CTRL_I((char) 0x09, false),
  CTRL_T((char) 0x14, false),
  
  // save and load
  CTRL_S((char) 0x13, false),
  CTRL_L((char) 0x0c, false);
  
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
  
  private Key(final char defaultMapping, final boolean isOptionKey) {
    this.mapping = KeyMapping.getMappingFor(name(), defaultMapping);
    this.isOptionKey = isOptionKey;
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
