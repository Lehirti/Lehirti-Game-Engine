package npc;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;

import lge.events.Event;
import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImageKey;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;


public class NPCSelectEvent extends EventNode<NullState> {
  
  private ImageKey backgroundImage;
  private Event<?> returnEvent;
  private final List<NPC> npcs;
  private int selectedNPC;
  
  // for saving/loading
  public NPCSelectEvent() {
    this(null, null, new LinkedList<NPC>(), 0);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.backgroundImage = (ImageKey) in.readObject();
    this.returnEvent = (Event<?>) in.readObject();
    final int numberOfNPCs = in.readInt();
    for (int i = 0; i < numberOfNPCs; i++) {
      this.npcs.add((NPC) in.readObject());
    }
    this.selectedNPC = in.readInt();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.backgroundImage);
    out.writeObject(this.returnEvent);
    out.writeInt(this.npcs.size());
    for (final NPC npc : this.npcs) {
      out.writeObject(npc);
    }
    out.writeInt(this.selectedNPC);
  }
  
  public NPCSelectEvent(final ImageKey backgroundImage, final Event<?> returnEvent, final List<NPC> npcs,
      final int selectedNPC) {
    this.backgroundImage = backgroundImage;
    this.returnEvent = returnEvent;
    this.npcs = npcs;
    this.selectedNPC = selectedNPC;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    if (this.npcs.isEmpty()) {
      return ImgChange.setBG(this.backgroundImage);
    } else {
      return ImgChange.setBGAndFG(this.backgroundImage, this.npcs.get(this.selectedNPC).getImage());
    }
  }
  
  @Override
  protected void doEvent() {
    setText((TextKey) null); // TODO ?
    
    for (int i = 0; i < this.npcs.size(); i++) {
      if (i == this.selectedNPC) {
        addText(CommonText.MARKER);
      }
      addText(this.npcs.get(i).getNameTextWrapper());
      addText(CommonText.NEWLINE);
    }
    
    if (!this.npcs.isEmpty()) {
      final NPC selNPC = this.npcs.get(this.selectedNPC);
      addOption(Key.OPTION_ENTER, selNPC.getNameTextWrapper(), new NPCOverviewEvent(selNPC,
          new NPCSelectEvent(this.backgroundImage, this.returnEvent, this.npcs, this.selectedNPC)));
      if (this.npcs.size() > 1) {
        int prev = this.selectedNPC - 1;
        if (prev < 0) {
          prev = this.npcs.size() - 1;
        }
        int next = this.selectedNPC + 1;
        if (next > this.npcs.size() - 1) {
          next = 0;
        }
        addOption(Key.OPTION_NORTH, CommonText.OPTION_PREVIOUS, new NPCSelectEvent(this.backgroundImage,
            this.returnEvent, this.npcs, prev));
        addOption(Key.OPTION_SOUTH, CommonText.OPTION_NEXT, new NPCSelectEvent(this.backgroundImage, this.returnEvent,
            this.npcs, next));
      }
    }
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK, this.returnEvent);
  }
}
