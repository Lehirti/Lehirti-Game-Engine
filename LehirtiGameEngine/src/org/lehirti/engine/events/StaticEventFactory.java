package org.lehirti.engine.events;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticEventFactory implements EventFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(StaticEventFactory.class);
  
  private final Constructor<? extends Event<?>> eventConstructor;
  private final Class<? extends Event<?>> eventToCreate;
  
  /**
   * @param eventToCreate
   *          this event class HAS TO HAVE A DEFAULT CONSTRUCTOR or the app will crash at runtime
   */
  public StaticEventFactory(final Class<? extends Event<?>> eventToCreate) {
    this.eventToCreate = eventToCreate;
    try {
      this.eventConstructor = eventToCreate.getConstructor();
    } catch (final SecurityException e) {
      // should not happen in standalone java apps
      throw new RuntimeException(e);
    } catch (final NoSuchMethodException e) {
      LOGGER.error("This class cannot create the events it's supposed create since the event class \""
          + eventToCreate.getName()
          + "\" does not have a default constructor. This is a programming error that has to be fixed.", e);
      throw new RuntimeException(e);
    }
  }
  
  public Event<?> getInstance() {
    try {
      return this.eventConstructor.newInstance();
    } catch (final IllegalArgumentException e) {
      // should not happen: the constructor of this class has made sure the default constructor exists
      throw new RuntimeException(e);
    } catch (final InstantiationException e) {
      LOGGER.error("This class cannot create the events it's supposed create since the event class \""
          + this.eventToCreate.getName() + "\" is abstract. This is a programming error that has to be fixed.", e);
      throw new RuntimeException(e);
    } catch (final IllegalAccessException e) {
      LOGGER.error("This class cannot create the events it's supposed create since the event class \""
          + this.eventToCreate.getName()
          + "\" default constructor is not public. This is a programming error that has to be fixed.", e);
      throw new RuntimeException(e);
    } catch (final InvocationTargetException e) {
      LOGGER.error("This class cannot create the events it's supposed create since the event class \""
          + this.eventToCreate.getName() + "\" default constructor has thrown an exception.", e);
      throw new RuntimeException(e);
    }
  }
}
