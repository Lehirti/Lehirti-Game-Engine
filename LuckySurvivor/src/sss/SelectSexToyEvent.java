package sss;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import lge.events.Event;
import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

import npc.NPC;

public class SelectSexToyEvent extends EventNode<NullState> {
  
  private NPC npc;
  private SexAct act;
  private List<SexToy> allToys;
  private int selectedSexToy;
  private Event<?> returnEvent;
  
  // for saving/loading
  public SelectSexToyEvent() {
    this(null, null, null, 0, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.npc = (NPC) in.readObject();
    this.act = (SexAct) in.readObject();
    this.allToys = (List<SexToy>) in.readObject();
    this.selectedSexToy = in.readInt();
    this.returnEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.npc);
    out.writeObject(this.act);
    out.writeObject(this.allToys);
    out.writeInt(this.selectedSexToy);
    out.writeObject(this.returnEvent);
  }
  
  public SelectSexToyEvent(final NPC npc, final SexAct act, final List<SexToy> allToys, final int selectedToy,
      final Event<?> returnEvent) {
    this.npc = npc;
    this.act = act;
    this.allToys = allToys;
    this.selectedSexToy = selectedToy;
    this.returnEvent = returnEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(this.allToys.get(this.selectedSexToy));
  }
  
  @Override
  protected void doEvent() {
    setText((TextKey) null);
    for (int i = 0; i < this.allToys.size(); i++) {
      if (i == this.selectedSexToy) {
        addText(CommonText.MARKER);
      }
      addText(this.allToys.get(i));
    }
    
    final List<SexAct> availableSexActs = this.npc.getAvailableSexActs();
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK,
        new SelectSexAct(this.npc, availableSexActs, this.act.getSelectedIndex(availableSexActs), this.returnEvent));
    int previous = this.selectedSexToy - 1;
    if (previous < 0) {
      previous = this.allToys.size() - 1;
    }
    int next = this.selectedSexToy + 1;
    if (next >= this.allToys.size()) {
      next = 0;
    }
    addOption(Key.OPTION_NORTH, CommonText.OPTION_PREVIOUS, new SelectSexToyEvent(this.npc, this.act, this.allToys,
        previous, this.returnEvent));
    addOption(Key.OPTION_SOUTH, CommonText.OPTION_NEXT, new SelectSexToyEvent(this.npc, this.act, this.allToys, next,
        this.returnEvent));
    
    final SexToy selectedToy = this.allToys.get(this.selectedSexToy);
    if (ReactionToSexAct.getEffective(this.npc.getReactionToPropositionOf(this.act, selectedToy)).ordinal() > ReactionToSexAct.INDIFFERENT
        .ordinal()) {
      addOption(Key.OPTION_ENTER, CommonText.OPTION_USE_IT, new PerformSexActEvent(this.npc, this.act, selectedToy,
          this.returnEvent));
    } else {
      addOption(Key.OPTION_ENTER, CommonText.OPTION_USE_IT, new ProposeSexActEvent(this.npc, this.act, selectedToy,
          this.returnEvent));
    }
  }
}
