package intro;

import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.TextAndImageKey;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.state.State;

public class Credits extends EventNode<NullState> {
  public static enum Contributor implements TextAndImageKey {
    HASORI,
    LEHIRTI,
    NIGHTARIX;
  }
  
  private final Contributor contributor;
  
  // for load/save
  public Credits() {
    this.contributor = Contributor.values()[State.DIE.nextInt(Contributor.values().length)];
  }
  
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
        .ordinal() + 1 : 0)];
    
    addOption(Key.OPTION_NORTH, CommonText.OPTION_PREVIOUS, new Credits(previous));
    addOption(Key.OPTION_SOUTH, CommonText.OPTION_NEXT, new Credits(next));
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK, new Startscreen());
  }
}
