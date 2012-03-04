package org.lehirti.state;

/**
 * All Subclasses are supposed to be enums
 */
public interface State {
  public String name();
  
  public Object defaultValue();
}
