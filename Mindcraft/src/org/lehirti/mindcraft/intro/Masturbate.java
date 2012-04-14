package org.lehirti.mindcraft.intro;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.text.TextKey;

public class Masturbate extends EventNode<NullState> {
  
  // START GENERATED CODE BLOCK
  @Override
  public Collection<ImageKey> getAllUsedImages() {
    final Set<ImageKey> allUsedImages = new HashSet<ImageKey>();
    allUsedImages.add(Intro.MASTURBATE);
    return allUsedImages;
  };
  
  // END GENERATED CODE BLOCK
  
  public static enum Text implements TextKey {
    TEXT
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TEXT);
    setImage(Intro.MASTURBATE);
    set(Bool.YOU_ARE_HORNY, false);
    
    addOption(HealerHut.Text.VISIT_HEALER, new HealerHut());
  }
}
