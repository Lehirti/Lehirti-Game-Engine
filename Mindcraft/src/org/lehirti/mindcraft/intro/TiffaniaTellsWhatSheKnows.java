package org.lehirti.mindcraft.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.mindcraft.images.TiffaniaWestwood;

public class TiffaniaTellsWhatSheKnows extends EventNode<NullState> {
  public static enum Text implements TextKey {
    EXPLANATION
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(TiffaniaWestwood.TALKS_TO_YOU);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.EXPLANATION);
    
    addOption(HealerHut.Text.VISIT_HEALER, new HealerHut());
  }
}
