package org.lehirti.engine.events;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Main;
import org.lehirti.engine.res.images.ImgChange;

public final class AltScreenToGameEvent extends EventNode<NullState> {
  public AltScreenToGameEvent() {
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    Main.setCurrentAreas(null);
  }
}
