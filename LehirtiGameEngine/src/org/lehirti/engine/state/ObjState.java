package org.lehirti.engine.state;

import java.io.Serializable;

public interface ObjState extends State {
  public Serializable defaultValue();
}
