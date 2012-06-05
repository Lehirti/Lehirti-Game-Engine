package org.lehirti.luckysurvivor.sss;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Option;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.luckysurvivor.npc.NPC;
import org.lehirti.luckysurvivor.npc.NPCHaveSex;

public class ProposeSexActEvent extends EventNode<NullState> implements Externalizable {
  
  private NPC npc;
  private SexAct act;
  private Event<?> returnEvent;
  
  // for saving/loading
  public ProposeSexActEvent() {
    this(null, null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.npc = (NPC) in.readObject();
    this.act = (SexAct) in.readObject();
    this.returnEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.npc);
    out.writeObject(this.act);
    out.writeObject(this.returnEvent);
  }
  
  public ProposeSexActEvent(final NPC npc, final SexAct act, final Event<?> returnEvent) {
    this.npc = npc;
    this.act = act;
    this.returnEvent = returnEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    final int npcDispositionToSexAct = this.npc.getDispositionTo(this.act, false);
    return ImgChange.setFG(this.npc.getReactionImage(this.act, npcDispositionToSexAct));
  }
  
  @Override
  protected void doEvent() {
    setText(this.act);
    
    final int npcDispositionToSexAct = this.npc.getDispositionTo(this.act, true);
    for (final TextWrapper txtWrp : this.npc.getReactionText(this.act, npcDispositionToSexAct)) {
      addText(txtWrp);
    }
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK, new NPCHaveSex(this.npc, this.returnEvent));
    for (final Option option : this.npc.getSexActsOptions(this.returnEvent)) {
      addOption(option.key, option.text, option.event);
    }
  }
}
