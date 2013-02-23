package org.lehirti.luckysurvivor.intro;

import java.awt.event.KeyEvent;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;

public class ChangeKeyMapping extends EventNode<NullState> {
  public static enum Text implements TextKey {
    CHANGE_KEY;
  }
  
  private final Key key;
  
  public ChangeKeyMapping() {
    this(Key.values()[0]);
  }
  
  public ChangeKeyMapping(final Key key) {
    this.key = key;
  }
  
  @Override
  protected void doEvent() {
    setText(Text.CHANGE_KEY);
    addText(CommonText.NEW_PARAGRAPH);
    final TextWrapper keyDesc = ResourceCache.get(CommonText.PARAMETER);
    keyDesc.addParameter(this.key.getKeyText());
    addText(keyDesc);
    addText(CommonText.NEW_PARAGRAPH);
    addText(this.key.getKeyFunction());
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK, new Controls(this.key));
  }
  
  @Override
  public boolean handleKeyEvent(final KeyEvent e) {
    this.key.setCodeAndModifiers(e.getKeyCode(), e.getModifiers());
    return handleKeyEvent(Key.OPTION_LEAVE);
  }
}
