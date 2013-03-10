package org.lehirti.mindcraft.intro;

import lge.events.StandardEvent;
import lge.gui.Key;
import lge.res.text.CommonText;

import org.lehirti.mindcraft.images.Background;

public class Market extends StandardEvent {
  private static final long serialVersionUID = 1L;
  
  public Market() {
    super(Key.OPTION_LEAVE, Background.VILLAGE_MARKET, CommonText.OPTION_LEAVE_AREA, new HomeVillage());
  }
}
