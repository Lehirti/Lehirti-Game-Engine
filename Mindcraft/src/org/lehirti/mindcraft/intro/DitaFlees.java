package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
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
