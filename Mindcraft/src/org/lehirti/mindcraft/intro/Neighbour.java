package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.mindcraft.images.Background;

public class Neighbour extends StandardEvent {
  private static final long serialVersionUID = 1L;
  
  public Neighbour() {
    super(Key.OPTION_LEAVE, Background.VILLAGE_NEIGHBOUR, CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
