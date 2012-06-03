package org.lehirti.engine.events;

import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.TextKey;

public final class Option {
  public Key key;
  public TextKey text;
  public Event<?> event;
  
  public Option(final Key key, final TextKey text, final Event<?> event) {
    this.key = key;
    this.text = text;
    this.event = event;
  }
}
