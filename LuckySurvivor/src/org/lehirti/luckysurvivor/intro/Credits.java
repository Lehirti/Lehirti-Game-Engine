package org.lehirti.luckysurvivor.intro;

import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.TextAndImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;

public class Credits extends EventNode<NullState> {
  public static enum Contributor implements TextAndImageKey {
    HASORI,
    LEHIRTI,
    NIGHTARIX;
  }
  
  private final Contributor contributor;
  
  public Credits(final Contributor contributor) {
    this.contributor = contributor;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setBG(this.contributor);
  }
  
  @Override
  protected void doEvent() {
    setText(this.contributor);
    
    final Contributor previous = Contributor.values()[(this.contributor.ordinal() - 1 < 0 ? Contributor.values().length - 1
        : this.contributor.ordinal() - 1)];
    final Contributor next = Contributor.values()[(this.contributor.ordinal() + 1 < Contributor.values().length ? this.contributor
        .ordinal() + 1
        : 0)];
    
    addOption(Key.OPTION_NORTH, CommonText.OPTION_PREVIOUS, new Credits(previous));
    addOption(Key.OPTION_SOUTH, CommonText.OPTION_NEXT, new Credits(next));
    
    addOption(Key.OPTION_ENTER, CommonText.OPTION_BACK, new Startscreen());
  }
}
