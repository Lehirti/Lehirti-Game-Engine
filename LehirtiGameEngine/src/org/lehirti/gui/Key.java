package org.lehirti.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public enum Key {
  PREVIOUS('r', true),
  NEXT('f', true),
  CTRL_I((char) 0x09, false),
  CTRL_T((char) 0x14, false);
  
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
