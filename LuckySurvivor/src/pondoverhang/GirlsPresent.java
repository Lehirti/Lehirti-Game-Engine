package pondoverhang;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

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
