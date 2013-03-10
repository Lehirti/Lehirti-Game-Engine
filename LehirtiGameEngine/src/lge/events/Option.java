package lge.events;

import lge.gui.Key;
import lge.res.text.TextKey;

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
