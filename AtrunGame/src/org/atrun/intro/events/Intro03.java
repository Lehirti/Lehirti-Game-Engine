package org.atrun.intro.events;

import org.lehirti.events.EventNode;
import org.lehirti.gui.Key;

public class Intro03 extends EventNode {
  @Override
  protected void doEvent() {
    setText("That's it for now!");
    
    setInputOption(Key.PREVIOUS, new Intro02());
  }
}
