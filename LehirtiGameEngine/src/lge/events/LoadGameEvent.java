package lge.events;

import java.io.File;

import lge.events.Event.NullState;
import lge.gui.EngineMain;
import lge.gui.Key;
import lge.res.images.ImgChange;

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
    EngineMain.setCurrentAreas(Key.SHOW_MAIN_SCREEN);
  }
}
