package org.lehirti.modules.intro;

import org.lehirti.Key;
import org.lehirti.events.EventNode;

public class Intro01 extends EventNode {
  @Override
  protected void doEvent() {
    setText("Hallo");
    setImage(IntroImage.INTRO_01);
    
    setInputOption(Key.NEXT, new Intro02());
  }
}
