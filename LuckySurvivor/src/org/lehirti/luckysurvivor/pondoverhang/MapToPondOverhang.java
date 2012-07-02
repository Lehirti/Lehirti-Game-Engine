package org.lehirti.luckysurvivor.pondoverhang;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.RandomStandardEvent;
import org.lehirti.engine.events.SetFlagTextOnlyEvent;
import org.lehirti.engine.events.StandardEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.TextAndImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.map.Map;
import org.lehirti.luckysurvivor.map.Map.Location;

public class MapToPondOverhang extends EventNode<NullState> {
  public static enum Text implements TextKey {
    DESCRIPTION,
    LEAVE_THE_AREA,
    OPTION_OOGLE,
    OPTION_TAKE_A_BATH,
  }
  
  public static enum TxtImg implements TextAndImageKey {
    BATHING_GIRL_1,
    BATHING_GIRL_2,
    TAKE_A_BATH,
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBGAndFG(PondOverhang.POND_OVERHANG);
  }
  
  @Override
  protected void doEvent() {
    setText(Text.DESCRIPTION);
    
    // I Try to implement this option only if the (repeatable) GIRLS_PRESENT event happened
    if (is(PondOverhangBool.GIRLS_PRESENT)) {
      addOption(Key.OPTION_NORTH, Text.OPTION_OOGLE, new RandomStandardEvent(Key.OPTION_NORTH, new MapToPondOverhang(),
          TxtImg.BATHING_GIRL_1, TxtImg.BATHING_GIRL_2));
    }
    // If no girl is present, you can bathe alone
    else {
      /**
       * hasori, you seemed to have trouble creating new standard event instances. a short java background:<br/>
       * - every .java file is a class<br/>
       * - you can create an instance of a class with the "new" operator, followed by the class name and parameters in
       * parentheses<br/>
       * - this calls a "Constructor" of the class. if no constructors are defined, there always is a
       * "Standard Constructor" with no arguments (example: this class; so you can create a new MapToPondOverhang
       * instance by writing: "new MapToPondOverhang()")<br/>
       * - if at least one constructor is defined (there can be multiple), you must call one of those. you do that by
       * passing the right number of parameters with the right type in the right order.<br/>
       * - to see which parameters you need, you can place the cursor on the name of the class and press "F3". try it
       * out (use: StandardEvent)!
       */
      addOption(Key.OPTION_NORTH, Text.OPTION_TAKE_A_BATH, new StandardEvent(Key.OPTION_NORTH, TxtImg.TAKE_A_BATH,
          TxtImg.TAKE_A_BATH, new MapToPondOverhang()));
      
    }
    
    // the leaving option should always be present
    // when leaving, unset the GIRLS_PRESENT flag, so next time the player comes here, no girls might be present
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_LEAVE_AREA, new SetFlagTextOnlyEvent(PondOverhangBool.GIRLS_PRESENT,
        false, Key.OPTION_LEAVE, Text.LEAVE_THE_AREA, new Map(Location.POND_OVERHANG)));
  }
}
