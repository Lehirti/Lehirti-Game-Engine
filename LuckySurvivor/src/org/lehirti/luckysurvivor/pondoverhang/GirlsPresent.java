package org.lehirti.luckysurvivor.pondoverhang;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class GirlsPresent extends EventNode<NullState> {
  public static enum Text implements TextKey {
    GIRLS_PRESENT
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(PondOverhang.GIRLS_PRESENT);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.GIRLS_PRESENT);
    //I want the option "oogle" to only appear if this event has happened and thus girls are present at the pond.
    //So I added a bool and it should be set true if this event happened.
    set(PondOverhangBool.GIRLS_PRESENT, true);
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_NEXT, new MapToPondOverhang());
  }
}
