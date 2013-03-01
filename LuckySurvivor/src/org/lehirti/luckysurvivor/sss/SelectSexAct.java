package org.lehirti.luckysurvivor.sss;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Option;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.npc.NPC;

public class SelectSexAct extends EventNode<NullState> {
  
  public static enum Text implements TextKey {
    TEXT_SELECT_SEX_ACT,
    OPTION_END_SEX_SESSION;
  }
  
  private NPC npc;
  private final List<SexAct> availableSexActs;
  private int selectedSexActIndex;
  private Event<?> returnEvent;
  
  // for saving/loading
  public SelectSexAct() {
    this(null, new LinkedList<SexAct>(), 0, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.npc = (NPC) in.readObject();
    final int nrOfSexActs = in.readInt();
    for (int i = 0; i < nrOfSexActs; i++) {
      this.availableSexActs.add((SexAct) in.readObject());
    }
    this.selectedSexActIndex = in.readInt();
    this.returnEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.npc);
    out.writeInt(this.availableSexActs.size());
    for (int i = 0; i < this.availableSexActs.size(); i++) {
      out.writeObject(this.availableSexActs.get(i));
    }
    out.writeObject(this.returnEvent);
  }
  
  public SelectSexAct(final NPC npc, final List<SexAct> availableSexActs, final int selectedSexAct,
      final Event<?> returnEvent) {
    this.npc = npc;
    this.availableSexActs = availableSexActs;
    this.selectedSexActIndex = selectedSexAct;
    this.returnEvent = returnEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    if (this.selectedSexActIndex >= 0 && this.selectedSexActIndex < this.availableSexActs.size()) {
      return ImgChange.setFG(this.availableSexActs.get(this.selectedSexActIndex));
    } else {
      return ImgChange.clearFG();
    }
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TEXT_SELECT_SEX_ACT);
    
    for (int i = 0; i < this.availableSexActs.size(); i++) {
      if (i == this.selectedSexActIndex) {
        addText(CommonText.MARKER);
      }
      addText(this.availableSexActs.get(i));
      addText(CommonText.NEWLINE);
    }
    
    if (!this.availableSexActs.isEmpty()) {
      if (this.selectedSexActIndex < 0) {
        this.selectedSexActIndex = 0;
      }
      
      final SexAct selectedSexAct = this.availableSexActs.get(this.selectedSexActIndex);
      final Option chooseSexActOption = this.npc.createOption(selectedSexAct, null, this.returnEvent);
      addOption(Key.OPTION_ENTER, chooseSexActOption.text, chooseSexActOption.event);
      if (this.availableSexActs.size() > 1) {
        int prev = this.selectedSexActIndex - 1;
        if (prev < 0) {
          prev = this.availableSexActs.size() - 1;
        }
        int next = this.selectedSexActIndex + 1;
        if (next > this.availableSexActs.size() - 1) {
          next = 0;
        }
        addOption(Key.OPTION_NORTH, CommonText.OPTION_PREVIOUS, new SelectSexAct(this.npc, this.availableSexActs, prev,
            this.returnEvent));
        addOption(Key.OPTION_SOUTH, CommonText.OPTION_NEXT, new SelectSexAct(this.npc, this.availableSexActs, next,
            this.returnEvent));
      }
    }
    
    addOption(Key.OPTION_Q, Text.OPTION_END_SEX_SESSION, new EndSexSession(this.npc, this.returnEvent));
  }
}
