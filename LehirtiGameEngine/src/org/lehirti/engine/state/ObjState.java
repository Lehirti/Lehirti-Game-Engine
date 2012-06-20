package org.lehirti.engine.state;

import java.io.Serializable;

public interface ObjState extends AbstractState {
  public Serializable defaultValue();
}
