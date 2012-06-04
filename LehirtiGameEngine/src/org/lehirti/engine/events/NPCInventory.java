package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.state.NPC;

public class NPCInventory extends EventNode<NullState> implements Externalizable {
  
  private NPC npc;
  private Event<?> returnEvent;
  
  // for saving/loading
  public NPCInventory() {
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
  
  public NPCInventory(final NPC npc, final Event<?> returnEvent) {
    this.npc = npc;
    this.returnEvent = returnEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(this.npc.getImage());
  }
  
  @Override
  protected void doEvent() {
    setText(this.npc.getName());
    for (final TextWrapper txtWrp : this.npc.getInventoryDescription()) {
      addText(txtWrp);
    }
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK, new NPCOverviewEvent(this.npc, this.returnEvent));
    for (final Option option : this.npc.getInventoryOptions(this.returnEvent)) {
      addOption(option.key, option.text, option.event);
    }
  }
}
