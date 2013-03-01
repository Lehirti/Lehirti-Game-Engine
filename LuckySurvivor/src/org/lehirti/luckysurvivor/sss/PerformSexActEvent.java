package org.lehirti.luckysurvivor.sss;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Option;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.state.DateTime;
import org.lehirti.luckysurvivor.npc.NPC;
import org.lehirti.luckysurvivor.pc.PC;

public class PerformSexActEvent extends EventNode<NullState> {
  
  private NPC npc;
  private SexAct act;
  private SexToy toy;
  private Event<?> returnEvent;
  
  // for saving/loading
  public PerformSexActEvent() {
    this(null, null, null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.npc = (NPC) in.readObject();
    this.act = (SexAct) in.readObject();
    this.toy = (SexToy) in.readObject();
    this.returnEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.npc);
    out.writeObject(this.act);
    out.writeObject(this.toy);
    out.writeObject(this.returnEvent);
  }
  
  public PerformSexActEvent(final NPC npc, final SexAct act, final SexToy toy, final Event<?> returnEvent) {
    this.npc = npc;
    this.act = act;
    this.toy = toy;
    this.returnEvent = returnEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(this.npc.getSexActPerformedImage(this.act, this.toy));
  }
  
  @Override
  protected void doEvent() {
    setText(this.act);
    
    DateTime.advanceBy(this.act.timeDDhhmmss);
    
    PC.PLAYER.performSexAct(this.act, this.toy);
    this.npc.performSexAct(this.act, this.toy);
    
    for (final TextWrapper txtWrp : this.npc.getSexActPerformedText(this.act, this.toy)) {
      addText(txtWrp);
    }
    
    for (final Option option : this.npc.getSexActPerformedOptions(this.act, this.toy, this.returnEvent)) {
      addOption(option.key, option.text, option.event);
    }
  }
}
