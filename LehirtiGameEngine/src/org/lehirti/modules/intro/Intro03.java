package org.lehirti.modules.intro;

import org.lehirti.Key;
import org.lehirti.events.EventNode;

public class Intro03 extends EventNode {
  @Override
  protected void doEvent() {
    setText("That's it for now!");
    
    setInputOption(Key.PREVIOUS, new Intro02());
  }
}
