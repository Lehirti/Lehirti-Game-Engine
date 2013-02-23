package org.lehirti.engine.gui;

import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.images.CommonImage;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.text.TextFunction;
import org.lehirti.engine.res.text.TextKey;

public enum Key {
  OPTION_Q(81, 0, TextFunction.OPTION, 0, 0, CommonImage.OPTION_KEY_0_0),
  OPTION_LEAVE(87, 0, TextFunction.OPTION, 1, 0, CommonImage.OPTION_KEY_1_0),
  OPTION_NORTH(69, 0, TextFunction.OPTION, 2, 0, CommonImage.OPTION_KEY_2_0),
  OPTION_ENTER(82, 0, TextFunction.OPTION, 3, 0, CommonImage.OPTION_KEY_3_0),
  OPTION_A(65, 0, TextFunction.OPTION, 0, 1, CommonImage.OPTION_KEY_0_1),
  OPTION_WEST(83, 0, TextFunction.OPTION, 1, 1, CommonImage.OPTION_KEY_1_1),
  OPTION_SOUTH(68, 0, TextFunction.OPTION, 2, 1, CommonImage.OPTION_KEY_2_1),
  OPTION_EAST(70, 0, TextFunction.OPTION, 3, 1, CommonImage.OPTION_KEY_3_1),
  OPTION_Z(90, 0, TextFunction.OPTION, 0, 2, CommonImage.OPTION_KEY_0_2),
  OPTION_X(88, 0, TextFunction.OPTION, 1, 2, CommonImage.OPTION_KEY_1_2),
  OPTION_C(67, 0, TextFunction.OPTION, 2, 2, CommonImage.OPTION_KEY_2_2),
  OPTION_V(86, 0, TextFunction.OPTION, 3, 2, CommonImage.OPTION_KEY_3_2),
  
  TEXT_INPUT_OPTION_ENTER(10, 0, TextFunction.ENTER),
  
  //
  SHOW_INVENTORY(49, 0, TextFunction.SHOW_INVENTORY),
  
  SHOW_PROGRESS(50, 0, TextFunction.SHOW_PROGRESS),
  
  CYCLE_TEXT_PAGES(32, 0, TextFunction.CYCLE_TEXT_PAGES),
  
  // image and text editors
  IMAGE_EDITOR(73, InputEvent.CTRL_MASK, TextFunction.IMAGE_EDITOR),
  TEXT_EDITOR(84, InputEvent.CTRL_MASK, TextFunction.TEXT_EDITOR),
  
  // save and load
  SAVE(83, InputEvent.CTRL_MASK, TextFunction.SAVE),
  LOAD(76, InputEvent.CTRL_MASK, TextFunction.LOAD);
  
  private static final List<Key> OPTION_KEYS = new ArrayList<Key>(values().length);
  
  static {
    KeyMapping.store();
    for (final Key key : values()) {
      if (key.isOptionKey) {
        OPTION_KEYS.add(key);
      }
    }
  }
  
  private int code;
  private int modifiers;
  private final TextKey textFunction;
  public final boolean isOptionKey;
  public final int col;
  public final int row;
  public final ImageKey buttonImageKey;
  private Image buttonImage;
  
  private Key(final int defaultCode, final int defaultModifiers, final TextKey textFunction, final int col,
      final int row, final ImageKey buttonImageKey) {
    this.code = KeyMapping.getMappingFor(name(), defaultCode);
    this.modifiers = KeyMapping.getMappingFor(name() + "mod", defaultModifiers);
    this.textFunction = textFunction;
    this.isOptionKey = true;
    this.col = col;
    this.row = row;
    this.buttonImageKey = buttonImageKey;
  }
  
  private Key(final int defaultCode, final int defaultModifiers, final TextKey textFunction) {
    this.code = KeyMapping.getMappingFor(name(), defaultCode);
    this.modifiers = KeyMapping.getMappingFor(name() + "mod", defaultModifiers);
    this.textFunction = textFunction;
    this.isOptionKey = false;
    this.col = -1;
    this.row = -1;
    this.buttonImageKey = null;
  }
  
  public Image getButtonImage() {
    if (this.buttonImage == null) {
      this.buttonImage = ResourceCache.get(this.buttonImageKey).getImage();
    }
    return this.buttonImage;
  }
  
  public TextKey getKeyFunction() {
    return this.textFunction;
  }
  
  public String getKeyText() {
    final StringBuilder sb = new StringBuilder();
    
    if ((this.modifiers & InputEvent.SHIFT_MASK) != 0) {
      sb.append("SHIFT ");
    }
    
    if ((this.modifiers & InputEvent.CTRL_MASK) != 0) {
      sb.append("CTRL ");
    }
    
    if ((this.modifiers & InputEvent.META_MASK) != 0) {
      sb.append("META ");
    }
    
    if ((this.modifiers & InputEvent.ALT_MASK) != 0) {
      sb.append("ALT ");
    }
    
    if ((this.modifiers & InputEvent.ALT_GRAPH_MASK) != 0) {
      sb.append("ALT GR ");
    }
    sb.append(KeyEvent.getKeyText(this.code));
    
    return sb.toString();
  }
  
  public int getCode() {
    return this.code;
  }
  
  public int getModifiers() {
    return this.modifiers;
  }
  
  public void setCodeAndModifiers(final int code, final int modifiers) {
    this.code = code;
    this.modifiers = modifiers;
    KeyMapping.store();
  }
  
  public static Key getByCodeAndModifiers(final int code, final int modifiers) {
    for (final Key key : values()) {
      if (key.code == code && key.modifiers == modifiers) {
        return key;
      }
    }
    return null;
  }
  
  public static List<Key> getOptionKeys() {
    final List<Key> optionKeys = new LinkedList<Key>(OPTION_KEYS);
    return optionKeys;
  }
}
