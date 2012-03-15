package org.lehirti.state;

import java.io.Serializable;

public interface ObjState extends State {
  public Serializable defaultValue();
}
