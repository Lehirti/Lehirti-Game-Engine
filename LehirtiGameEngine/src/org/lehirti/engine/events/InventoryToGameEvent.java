package org.lehirti.engine.events;

import java.io.Externalizable;

import org.lehirti.engine.Main;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;

public final class InventoryToGameEvent extends EventNode<NullState> implements Externalizable {
  public InventoryToGameEvent() {
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    Main.setCurrentImageArea(Main.IMAGE_AREA);
    Main.setCurrentTextArea(Main.TEXT_AREA);
    Main.setCurrentOptionArea(Main.OPTION_AREA);
  }
}
