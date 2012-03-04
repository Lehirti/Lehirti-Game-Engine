package org.lehirti.gui;

public enum Key {
  PREVIOUS('r'),
  NEXT('f'),
  CTRL_S((char) 0x13);
  
  static {
    KeyMapping.store();
  }
  
  public final char mapping;
  
  private Key(final char defaultMapping) {
    this.mapping = KeyMapping.getMappingFor(name(), defaultMapping);
  }
  
  public static Key getByChar(final char mapping) {
    for (final Key key : values()) {
      if (key.mapping == mapping) {
        return key;
      }
    }
    return null;
  }
}
