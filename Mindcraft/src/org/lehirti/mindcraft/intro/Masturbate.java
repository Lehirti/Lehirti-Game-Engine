package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.text.TextKey;

public class Masturbate extends EventNode<NullState> {
  public static enum Text implements TextKey {
    TEXT
  }
  
  @Override
  protected void loadImages() {
    ResourceCache.get(Intro.MASTURBATE);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TEXT);
    setImage(Intro.MASTURBATE);
    set(Bool.YOU_ARE_HORNY, false);
    
    addOption(HealerHut.Text.VISIT_HEALER, new HealerHut());
  }
}
