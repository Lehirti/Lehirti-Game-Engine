package npc;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lge.events.Event;
import lge.events.EventNode;
import lge.events.Option;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextWrapper;


public class NPCInventory extends EventNode<NullState> {
  
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
    setText(this.npc.getNameTextWrapper());
    for (final TextWrapper txtWrp : this.npc.getInventoryDescription()) {
      addText(txtWrp);
    }
    
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK, new NPCOverviewEvent(this.npc, this.returnEvent));
    for (final Option option : this.npc.getInventoryOptions(this.returnEvent)) {
      addOption(option.key, option.text, option.event);
    }
  }
}
