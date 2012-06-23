package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.mindcraft.images.Background;

public class Bathhouse extends StandardEvent {
  private static final long serialVersionUID = 1L;
  
  public Bathhouse() {
    super(Key.OPTION_ENTER, Background.VILLAGE_BATHHOUSE, CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
