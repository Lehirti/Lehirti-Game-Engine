package intro;

import java.awt.event.KeyEvent;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.ResourceCache;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.res.text.TextWrapper;


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
  public boolean handleKeyEvent(final KeyEvent e, final Key ignore) {
    this.key.setCodeAndModifiers(e.getKeyCode(), e.getModifiers());
    return handleKeyEvent(Key.OPTION_LEAVE);
  }
}
