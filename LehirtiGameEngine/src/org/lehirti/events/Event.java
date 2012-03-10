package org.lehirti.events;

import org.lehirti.gui.Key;

public interface Event {
  public void execute();
  
  public boolean handle(Key key);
}
