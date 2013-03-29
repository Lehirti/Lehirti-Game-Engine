package lge.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EventClassHelper {
  private static Logger LOGGER = LoggerFactory.getLogger(EventClassHelper.class);
  
  /**
   * 
   * 
   * @param withOnlyDefaultConstructorOnly
   * @return list of fully-qualified class names of all (or only those with only a default constructor) non-abstract
   *         event classes that are shipped with the game (ignores generated-from-xml classes in mod/events/bin)
   */
  public static List<String> getEventClassFQCNs(final boolean withOnlyDefaultConstructorOnly) {
    // TODO hard-coded pattern
    final Vector<Class<?>> eventClasses = new ClassFinder(".*mod.events.bin").findSubclasses(lge.events.Event.class
        .getName());
    final List<String> fqcnOfAllEvents = new LinkedList<>();
    for (final Class<?> eventClass : eventClasses) {
      if (Modifier.isAbstract(eventClass.getModifiers())) {
        continue; // skip abstract Events (base classes)
      }
      if (withOnlyDefaultConstructorOnly) {
        final Constructor<?>[] constructors = eventClass.getConstructors();
        if (constructors.length != 1) {
          continue; // must have at least one non-default constructor
        }
        if (constructors[0].getParameterTypes().length > 0) {
          continue; // is not default constructor
        }
      }
      fqcnOfAllEvents.add(eventClass.getName());
    }
    return fqcnOfAllEvents;
  }
}
