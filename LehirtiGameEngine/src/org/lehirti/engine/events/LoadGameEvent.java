package org.lehirti.engine.events;

import java.io.File;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.EngineMain;
import org.lehirti.engine.res.images.ImgChange;

public final class LoadGameEvent extends EventNode<NullState> {
  private File sav;
  
  public LoadGameEvent() {
  }
  
  public LoadGameEvent(final File sav) {
    this.sav = sav;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    EngineMain.loadGame(this.sav);
    EngineMain.setCurrentAreas(null);
  }
}
