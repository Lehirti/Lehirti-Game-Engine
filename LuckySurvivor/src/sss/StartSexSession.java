package sss;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lge.events.Event;
import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;

import npc.NPC;

public class StartSexSession extends EventNode<NullState> {
  
  public static enum Text implements TextKey {
    TEXT_START_SEX_SESSION,
    OPTION_SELECT_SEX_ACT;
  }
  
  private NPC npc;
  private Event<?> returnEvent;
  
  // for saving/loading
  public StartSexSession() {
    this(null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.npc = (NPC) in.readObject();
    this.returnEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.npc);
    out.writeObject(this.returnEvent);
  }
  
  public StartSexSession(final NPC npc, final Event<?> returnEvent) {
    this.npc = npc;
    this.returnEvent = returnEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.clearFG(); // TODO
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TEXT_START_SEX_SESSION);
    
    SexSession.start(this.npc);
    
    addOption(Key.OPTION_ENTER, Text.OPTION_SELECT_SEX_ACT, new SelectSexAct(this.npc, this.npc.getAvailableSexActs(),
        0, this.returnEvent));
  }
}
