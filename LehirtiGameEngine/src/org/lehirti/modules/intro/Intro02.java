package org.lehirti.modules.intro;

import org.lehirti.Key;
import org.lehirti.events.EventNode;

public class Intro02 extends EventNode {
  @Override
  protected void doEvent() {
    setText("Nice to meet you!");
    
    setInputOption(Key.PREVIOUS, new Intro01());
    setInputOption(Key.NEXT, new Intro03());
  }
}
