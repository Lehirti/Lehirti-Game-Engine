package org.lehirti.mindcraft.intro;

import lge.events.StandardEvent;
import lge.gui.Key;
import lge.res.text.CommonText;

import org.lehirti.mindcraft.images.Background;

public class Neighbour extends StandardEvent {
  private static final long serialVersionUID = 1L;
  
  public Neighbour() {
    super(Key.OPTION_LEAVE, Background.VILLAGE_NEIGHBOUR, CommonText.OPTION_LEAVE, new HomeVillage());
  }
}
