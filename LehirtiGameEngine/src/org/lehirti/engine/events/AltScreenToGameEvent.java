package org.lehirti.engine.events;

import java.io.Externalizable;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Main;
import org.lehirti.engine.res.images.ImgChange;

public final class AltScreenToGameEvent extends EventNode<NullState> implements Externalizable {
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
