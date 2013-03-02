package org.lehirti.engine.state;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.util.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class State implements Externalizable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger LOGGER = LoggerFactory.getLogger(State.class);
  
  public static final Random DIE = new Random();
  
  private static final State INSTANCE = new State();
  
  private static final Comparator<AbstractState> SORTER = new Comparator<AbstractState>() {
    
    @Override
    public int compare(final AbstractState o1, final AbstractState o2) {
      if (o1.getClass().equals(o2.getClass())) {
        return o1.name().compareTo(o2.name());
      }
      return o1.getClass().getName().compareTo(o2.getClass().getName());
    }
    
  };
  
  private static enum InternalGameState implements ObjState {
    EVENT_COUNT;
    
    @Override
    public Serializable defaultValue() {
      return null;
    }
  }
  
  // SortedMap produces class cast exceptions
  private final Map<BoolState, Boolean> BOOL_MAP = new InventoryMap<>();
  private final Map<IntState, Long> INT_MAP = new InventoryMap<>();
  private final Map<ObjState, Serializable> OBJ_MAP = new InventoryMap<>();
  private final Map<StringState, String> STRING_MAP = new InventoryMap<>();
  private final Map<Class<?>, EventState> PER_CLASS_STATE_MAP = new LinkedHashMap<>();
  
  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // initialize defaults
  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public static void initIntDefaults(final Class<IntState> intStatesClass) {
    initDefaults(intStatesClass, "0");
  }
  
  public static void initStringDefaults(final Class<StringState> stringStatesClass) {
    initDefaults(stringStatesClass, "");
  }
  
  public static void initBoolDefaults(final Class<BoolState> boolStatesClass) {
    initDefaults(boolStatesClass, "FALSE");
  }
  
  private static void initDefaults(final Class<? extends AbstractState> statesClass, final String defaultValue) {
    boolean updateNecessary = false;
    final Properties defaultProperties = PropertyUtils.getDefaultProperties(statesClass);
    final AbstractState[] states = statesClass.getEnumConstants();
    for (final AbstractState state : states) {
      if (!defaultProperties.containsKey(state.name())) {
        // key in default properties missing
        defaultProperties.setProperty(state.name(), defaultValue);
        updateNecessary = true;
      }
    }
    final Set<Object> keySet = new HashSet<>(defaultProperties.keySet());
    KEYS: for (final Object key : keySet) {
      for (final AbstractState state : states) {
        if (state.name().equals(key)) {
          continue KEYS;
        }
      }
      // key in default properties file no longer used;
      defaultProperties.remove(key);
      updateNecessary = true;
    }
    
    if (updateNecessary) {
      PropertyUtils.setDefaultProperties(statesClass, defaultProperties);
    }
  }
  
  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // static getters for all state
  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public static boolean is(final BoolState key) {
    Boolean value = INSTANCE.BOOL_MAP.get(key);
    if (value == null) {
      final Properties defaultProperties = PropertyUtils.getDefaultProperties(key.getClass());
      final String defaultValue = defaultProperties.getProperty(key.name());
      value = Boolean.valueOf(defaultValue);
    }
    return value.booleanValue();
  }
  
  public static void set(final BoolState key, final boolean value) {
    INSTANCE.BOOL_MAP.put(key, Boolean.valueOf(value));
  }
  
  public static long get(final IntState key) {
    Long value = INSTANCE.INT_MAP.get(key);
    if (value == null) {
      final Properties defaultProperties = PropertyUtils.getDefaultProperties(key.getClass());
      final String defaultValue = defaultProperties.getProperty(key.name());
      value = Long.valueOf(defaultValue);
    }
    return value.intValue();
  }
  
  public static void set(final IntState key, final long value) {
    INSTANCE.INT_MAP.put(key, Long.valueOf(value));
  }
  
  public static void change(final IntState key, final long delta) {
    INSTANCE.INT_MAP.put(key, Long.valueOf(get(key) + delta));
  }
  
  public static Serializable get(final ObjState key) {
    Serializable value = INSTANCE.OBJ_MAP.get(key);
    if (value == null) {
      value = key.defaultValue();
    }
    return value;
  }
  
  public static void set(final ObjState key, final Serializable value) {
    INSTANCE.OBJ_MAP.put(key, value);
  }
  
  public static String get(final StringState key) {
    String value = INSTANCE.STRING_MAP.get(key);
    if (value == null) {
      final Properties defaultProperties = PropertyUtils.getDefaultProperties(key.getClass());
      value = defaultProperties.getProperty(key.name());
    }
    return value;
  }
  
  public static void set(final StringState key, final String value) {
    INSTANCE.STRING_MAP.put(key, value);
  }
  
  public static EventState get(final Class<?> clazz) {
    return INSTANCE.PER_CLASS_STATE_MAP.get(clazz);
  }
  
  public static void set(final Class<?> clazz, final EventState value) {
    INSTANCE.PER_CLASS_STATE_MAP.put(clazz, value);
  }
  
  @SuppressWarnings("unchecked")
  public static void incrementEventCount(final Event<?> event) {
    Map<String, Integer> eventCountMap = (Map<String, Integer>) get(InternalGameState.EVENT_COUNT);
    if (eventCountMap == null) {
      eventCountMap = new TreeMap<>();
      set(InternalGameState.EVENT_COUNT, (Serializable) eventCountMap);
    }
    final String eventClassName = event.getClass().getName();
    Integer count = eventCountMap.get(eventClassName);
    if (count == null) {
      count = Integer.valueOf(1);
    } else {
      count = Integer.valueOf(count.intValue() + 1);
    }
    eventCountMap.put(eventClassName, count);
    
    LOGGER.debug("Event {} executed {} times.", eventClassName, count);
  }
  
  public static int getEventCount(final Event<?> event) {
    return getEventCount(event.getClass());
  }
  
  public static int getEventCount(final Class<? extends Event> eventClass) {
    final Map<String, Integer> eventCountMap = (Map<String, Integer>) get(InternalGameState.EVENT_COUNT);
    if (eventCountMap == null) {
      return 0;
    }
    
    final Integer count = eventCountMap.get(eventClass.getName());
    if (count == null) {
      return 0;
    }
    
    return count.intValue();
  }
  
  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Save/Load
  // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public static void save(final ObjectOutputStream oos) {
    try {
      oos.writeObject(INSTANCE);
    } catch (final IOException e) {
      LOGGER.error("Unable to save state", e);
    }
  }
  
  public static void load(final ObjectInputStream ois) {
    try {
      // this looks like a glaring bug at first, but calling readObject is enough to load the stored state
      ois.readObject();
    } catch (final IOException e) {
      LOGGER.error("Unable to load state", e);
    } catch (final ClassNotFoundException e) {
      LOGGER.error("Unable to load state", e);
    }
    
  }
  
  private static enum OnDiskDelim {
    
    START_BOOL_MAP,
    // there MUST NOT BE another constant between these two START/END constants
    END_BOOL_MAP,
    
    START_INT_MAP,
    // there MUST NOT BE another constant between these two START/END constants
    END_INT_MAP,
    
    START_OBJ_MAP,
    // there MUST NOT BE another constant between these two START/END constants
    END_OBJ_MAP,
    
    START_STRING_MAP,
    // there MUST NOT BE another constant between these two START/END constants
    END_STRING_MAP,
    
    START_PER_CLASS_STATE_MAP,
    // there MUST NOT BE another constant between these two START/END constants
    END_PER_CLASS_STATE_MAP,
    
    END_STATE_OBJECT;
    
    public OnDiskDelim getNext() {
      switch (this) {
      case START_BOOL_MAP:
        return END_BOOL_MAP;
      case START_INT_MAP:
        return END_INT_MAP;
      case START_OBJ_MAP:
        return END_OBJ_MAP;
      case START_STRING_MAP:
        return END_STRING_MAP;
      case START_PER_CLASS_STATE_MAP:
        return END_PER_CLASS_STATE_MAP;
      default:
        throw new RuntimeException("No clearly defined next delim for: " + this.name());
      }
    }
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeLong(serialVersionUID);
    out.writeObject(OnDiskDelim.START_BOOL_MAP.name());
    writeMap(out, this.BOOL_MAP);
    out.writeObject(OnDiskDelim.END_BOOL_MAP.name());
    out.writeObject(OnDiskDelim.START_INT_MAP.name());
    writeMap(out, this.INT_MAP);
    out.writeObject(OnDiskDelim.END_INT_MAP.name());
    out.writeObject(OnDiskDelim.START_OBJ_MAP.name());
    writeMap(out, this.OBJ_MAP);
    out.writeObject(OnDiskDelim.END_OBJ_MAP.name());
    out.writeObject(OnDiskDelim.START_STRING_MAP.name());
    writeMap(out, this.STRING_MAP);
    out.writeObject(OnDiskDelim.END_STRING_MAP.name());
    out.writeObject(OnDiskDelim.START_PER_CLASS_STATE_MAP.name());
    writePerClassMap(out, this.PER_CLASS_STATE_MAP);
    out.writeObject(OnDiskDelim.END_PER_CLASS_STATE_MAP.name());
    out.writeObject(OnDiskDelim.END_STATE_OBJECT.name());
  }
  
  private static void writePerClassMap(final ObjectOutput out, final Map<Class<?>, EventState> map) throws IOException {
    for (final Entry<Class<?>, EventState> entry : map.entrySet()) {
      out.writeObject(entry.getKey().getName());
      final EventState value = entry.getValue();
      out.writeObject(value.getClass().getName());
      out.writeObject(value.name());
    }
  }
  
  private static void writeMap(final ObjectOutput out, final Map<? extends AbstractState, ? extends Object> map)
      throws IOException {
    for (final Map.Entry<? extends AbstractState, ? extends Object> entry : map.entrySet()) {
      out.writeObject(entry.getKey().getClass().getName());
      out.writeObject(entry.getKey().name());
      out.writeObject(entry.getValue());
    }
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    final long version = in.readLong();
    if (version == 1L) {
      readObject1(in);
    } else {
      throw new RuntimeException("Unknown StateObject serialVersionUID: " + version);
    }
  }
  
  private static void readObject1(final ObjectInput in) throws IOException, ClassNotFoundException {
    INSTANCE.BOOL_MAP.clear();
    INSTANCE.INT_MAP.clear();
    INSTANCE.OBJ_MAP.clear();
    INSTANCE.STRING_MAP.clear();
    INSTANCE.PER_CLASS_STATE_MAP.clear();
    
    OnDiskDelim delim;
    while ((delim = OnDiskDelim.valueOf((String) in.readObject())) != OnDiskDelim.END_STATE_OBJECT) {
      readMap(in, delim);
    }
  }
  
  private static void readMap(final ObjectInput in, final OnDiskDelim startDelim) throws IOException,
      ClassNotFoundException {
    String className;
    final OnDiskDelim endDelim = startDelim.getNext();
    if (startDelim == OnDiskDelim.START_PER_CLASS_STATE_MAP) {
      while (!endDelim.name().equals((className = (String) in.readObject()))) {
        final String valueClassName = (String) in.readObject();
        final String valueName = (String) in.readObject();
        final Class<?> key;
        try {
          key = Class.forName(className);
          final EventState value = (EventState) Enum.valueOf((Class<? extends Enum>) Class.forName(valueClassName),
              valueName);
          INSTANCE.PER_CLASS_STATE_MAP.put(key, value);
        } catch (final RuntimeException | ClassNotFoundException e) {
          LOGGER.warn("Ignoring state " + valueClassName + "." + valueName + " for class " + className, e);
        }
      }
    } else {
      while (!endDelim.name().equals((className = (String) in.readObject()))) {
        final String keyName = (String) in.readObject();
        final Object value = in.readObject();
        
        try {
          final AbstractState key = (AbstractState) Enum.valueOf((Class<? extends Enum>) Class.forName(className),
              keyName);
          switch (startDelim) {
          case START_BOOL_MAP:
            INSTANCE.BOOL_MAP.put((BoolState) key, (Boolean) value);
            break;
          case START_INT_MAP:
            INSTANCE.INT_MAP.put((IntState) key, (Long) value);
            break;
          case START_OBJ_MAP:
            INSTANCE.OBJ_MAP.put((ObjState) key, (Serializable) value);
            break;
          case START_STRING_MAP:
            INSTANCE.STRING_MAP.put((StringState) key, (String) value);
            break;
          default:
            throw new RuntimeException("Unknown OnDiskDelim: " + startDelim.name());
          }
        } catch (final RuntimeException | ClassNotFoundException e) {
          LOGGER.warn("Ignoring unkown state {}.{}={}", new Object[] { className, keyName, value });
        }
      }
    }
  }
  
  public static String get(final AbstractState state) {
    if (state instanceof StringState) {
      return String.valueOf(get((StringState) state));
    } else if (state instanceof IntState) {
      return String.valueOf(get((IntState) state));
    } else if (state instanceof BoolState) {
      return String.valueOf(is((BoolState) state));
    }
    return "null"; // TODO
  }
}
