package lge.events;

import lge.events.Event.NullState;
import lge.gui.EngineMain;
import lge.res.images.ImgChange;

public final class AltScreenToGameEvent extends EventNode<NullState> {
  public AltScreenToGameEvent() {
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    EngineMain.setCurrentAreas(null);
  }
}
