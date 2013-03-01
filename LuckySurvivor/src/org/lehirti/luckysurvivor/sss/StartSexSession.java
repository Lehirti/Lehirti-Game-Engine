package org.lehirti.luckysurvivor.sss;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.npc.NPC;

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
