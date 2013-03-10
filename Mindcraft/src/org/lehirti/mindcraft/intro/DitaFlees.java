package org.lehirti.mindcraft.intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

import org.lehirti.mindcraft.images.Dita;

public class DitaFlees extends EventNode<NullState> {
  public static enum Text implements TextKey {
    MAIN,
    RUN_AFTER_HER
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(Dita.RUNS_AWAY);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.MAIN);
    
    addOption(Text.RUN_AFTER_HER, new FuckDitaInWoods());
  }
}
