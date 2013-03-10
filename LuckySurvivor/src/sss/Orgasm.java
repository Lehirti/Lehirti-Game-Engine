package sss;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lge.events.Event;
import lge.events.EventNode;
import lge.events.Option;
import lge.events.Event.NullState;
import lge.res.images.ImgChange;
import lge.res.text.TextWrapper;

import npc.NPC;

import pc.PC;

public class Orgasm extends EventNode<NullState> {
  
  private NPC npc;
  private SexAct act;
  private SexToy toy;
  private Event<?> returnEvent;
  
  // for saving/loading
  public Orgasm() {
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
  
  public Orgasm(final NPC npc, final SexAct act, final SexToy toy, final Event<?> returnEvent) {
    this.npc = npc;
    this.act = act;
    this.toy = toy;
    this.returnEvent = returnEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(this.npc.getOrgasmingImage(this.act, this.toy));
  }
  
  @Override
  protected void doEvent() {
    setText(this.act);
    
    if (PC.PLAYER.isOrgasming()) {
      SexSession.getCurrent().updateNPCPoints(PC.PLAYER.getArousal());
      PC.PLAYER.setArousal(PC.PLAYER.getArousal() / 3);
      
      for (final TextWrapper txtWrp : PC.PLAYER.getOrgasmingText(this.act, this.toy)) {
        addText(txtWrp);
      }
    }
    
    if (this.npc.isOrgasming()) {
      SexSession.getCurrent().updateNPCPoints(this.npc.getArousal());
      this.npc.setArousal(this.npc.getArousal() / 3);
      
      for (final TextWrapper txtWrp : PC.PLAYER.getOrgasmingText(this.act, this.toy)) {
        addText(txtWrp);
      }
    }
    
    for (final Option option : this.npc.getSexActPerformedOptions(this.act, this.toy, this.returnEvent)) {
      addOption(option.key, option.text, option.event);
    }
  }
}
