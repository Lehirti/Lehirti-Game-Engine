package org.lehirti.luckysurvivor.peninsulaisthmus;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class CreviceFurtherNorth extends EventNode<NullState> {
  public static enum Text implements TextKey {
    CREVICE_FURTHER_NORTH,
    OPTION_ESCAPE_CRAVICE
  }
  
  public static enum Img implements ImageKey {
    CREVICE_FURTHER_NORTH,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(Img.CREVICE_FURTHER_NORTH);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.CREVICE_FURTHER_NORTH);
    addOption(Key.OPTION_SOUTH, CommonText.OPTION_RETURN_SOUTH, new CreviceNorth());
    addOption(Key.OPTION_NORTH, Text.OPTION_ESCAPE_CRAVICE, new MapToPeninsulaIsthmus());
  }
}
