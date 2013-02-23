package org.lehirti.luckysurvivor.intro;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;

public class Controls extends EventNode<NullState> {
  public static enum Text implements TextKey {
    OPTION_CHANGE_KEY;
  }
  
  private final Key key;
  
  public Controls() {
    this(Key.values()[0]);
  }
  
  public Controls(final Key key) {
    this.key = key;
  }
  
  @Override
  protected void doEvent() {
    final TextWrapper keyDesc = ResourceCache.get(CommonText.PARAMETER);
    keyDesc.addParameter(this.key.getKeyText());
    setText(keyDesc);
    addText(CommonText.NEW_PARAGRAPH);
    addText(this.key.getKeyFunction());
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK, new Startscreen());
    
    int prevKeyOrd = this.key.ordinal() - 1;
    if (prevKeyOrd < 0) {
      prevKeyOrd = Key.values().length - 1;
    }
    final Key prevKey = Key.values()[prevKeyOrd];
    final TextWrapper prevKeyDesc = ResourceCache.get(CommonText.PARAMETER);
    prevKeyDesc.addParameter(prevKey.getKeyText());
    addOption(Key.OPTION_NORTH, prevKeyDesc, new Controls(prevKey));
    
    final int nextKeyOrd = (this.key.ordinal() + 1) % Key.values().length;
    final Key nextKey = Key.values()[nextKeyOrd];
    final TextWrapper nextKeyDesc = ResourceCache.get(CommonText.PARAMETER);
    nextKeyDesc.addParameter(nextKey.getKeyText());
    addOption(Key.OPTION_SOUTH, nextKeyDesc, new Controls(nextKey));
    
    addOption(Key.OPTION_ENTER, Text.OPTION_CHANGE_KEY, new ChangeKeyMapping(this.key));
  }
}
