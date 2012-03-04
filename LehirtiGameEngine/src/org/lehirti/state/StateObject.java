package org.lehirti.state;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StateObject implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private static final StateObject INSTANCE = new StateObject();
  
  // TODO turn into SortedMap
  private final Map<BoolState, Boolean> BOOL_MAP = new HashMap<BoolState, Boolean>();
  private final Map<IntState, Integer> INT_MAP = new HashMap<IntState, Integer>();
  private final Map<StringState, String> STRING_MAP = new HashMap<StringState, String>();
  
  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // static getters for all state
  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public static boolean is(final BoolState key) {
    Boolean value = INSTANCE.BOOL_MAP.get(key);
    if (value == null) {
      value = key.defaultValue();
    }
    return value.booleanValue();
  }
  
  public static void set(final BoolState key, final boolean value) {
    INSTANCE.BOOL_MAP.put(key, Boolean.valueOf(value));
  }
  
  public static int get(final IntState key) {
    Integer value = INSTANCE.INT_MAP.get(key);
    if (value == null) {
      value = key.defaultValue();
    }
    return value.intValue();
  }
  
  public static void set(final IntState key, final int value) {
    INSTANCE.INT_MAP.put(key, Integer.valueOf(value));
  }
  
  public static String get(final StringState key) {
    String value = INSTANCE.STRING_MAP.get(key);
    if (value == null) {
      value = key.defaultValue();
    }
    return value;
  }
  
  public static void set(final StringState key, final String value) {
    INSTANCE.STRING_MAP.put(key, value);
  }
  
  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Save/Load
  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static enum OnDiskDelim {
    
    START_BOOL_MAP,
    // the MUST NOT BE another constant between these two START/END constants
    END_BOOL_MAP,
    
    START_INT_MAP,
    // the MUST NOT BE another constant between these two START/END constants
    END_INT_MAP,
    
    START_STRING_MAP,
    // the MUST NOT BE another constant between these two START/END constants
    END_STRING_MAP,
    
    END_STATE_OBJECT;
    
    public OnDiskDelim getNext() {
      switch (this) {
      case START_BOOL_MAP:
        return END_BOOL_MAP;
      case START_INT_MAP:
        return END_INT_MAP;
      case START_STRING_MAP:
        return END_STRING_MAP;
      default:
        throw new RuntimeException("No clearly defined next delim for: " + this.name());
      }
    }
  }
  
  private void writeObject(final ObjectOutputStream out) throws IOException {
    out.writeLong(serialVersionUID);
    out.writeObject(OnDiskDelim.START_BOOL_MAP.name());
    writeMap(out, this.BOOL_MAP);
    out.writeObject(OnDiskDelim.END_BOOL_MAP.name());
    out.writeObject(OnDiskDelim.START_INT_MAP.name());
    writeMap(out, this.INT_MAP);
    out.writeObject(OnDiskDelim.END_INT_MAP.name());
    out.writeObject(OnDiskDelim.START_STRING_MAP.name());
    writeMap(out, this.STRING_MAP);
    out.writeObject(OnDiskDelim.END_STRING_MAP.name());
    out.writeObject(OnDiskDelim.END_STATE_OBJECT);
  }
  
  private void writeMap(final ObjectOutputStream out, final Map<? extends State, ? extends Object> map)
      throws IOException {
    for (final Map.Entry<? extends State, ? extends Object> entry : map.entrySet()) {
      out.writeObject(entry.getKey().getClass().getName());
      out.writeObject(entry.getKey().name());
      out.writeObject(entry.getValue());
    }
  }
  
  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    final long version = in.readLong();
    if (version == 1L) {
      readObject1(in);
    } else {
      throw new RuntimeException("Unknown StateObject serialVersionUID: " + version);
    }
  }
  
  private void readObject1(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    OnDiskDelim delim;
    while ((delim = OnDiskDelim.valueOf((String) in.readObject())) != OnDiskDelim.END_STATE_OBJECT) {
      readMap(in, delim);
    }
  }
  
  @SuppressWarnings("unchecked")
  private void readMap(final ObjectInputStream in, final OnDiskDelim startDelim) throws IOException,
      ClassNotFoundException {
    String className;
    final OnDiskDelim endDelim = startDelim.getNext();
    while (!endDelim.name().equals((className = (String) in.readObject()))) {
      final String keyName = (String) in.readObject();
      final Object value = in.readObject();
      
      try {
        final State key = (State) Enum.valueOf((Class<? extends Enum>) Class.forName(className), keyName);
        switch (startDelim) {
        case START_BOOL_MAP:
          this.BOOL_MAP.put((BoolState) key, (Boolean) value);
          break;
        case START_INT_MAP:
          this.INT_MAP.put((IntState) key, (Integer) value);
          break;
        case START_STRING_MAP:
          this.STRING_MAP.put((StringState) key, (String) value);
          break;
        default:
          throw new RuntimeException("Unknown OnDiskDelim: " + startDelim.name());
        }
      } catch (final RuntimeException e) {
        // state key probably does not exist anymore; TODO log as warn and continue
      }
    }
    
  }
}
