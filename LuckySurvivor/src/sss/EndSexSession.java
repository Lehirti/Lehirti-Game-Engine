package sss;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lge.events.Event;
import lge.events.EventNode;
import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.ResourceCache;
import lge.res.images.ImgChange;
import lge.res.text.TextKey;
import lge.res.text.TextWrapper;

import npc.NPC;
import npc.NPCOverviewEvent;

public class EndSexSession extends EventNode<NullState> {
  
  public static enum Text implements TextKey {
    TEXT_END_SEX_SESSION,
    TEXT_YOUR_SCORE,
    OPTION_END_SEX_SESSION;
  }
  
  private NPC npc;
  private Event<?> returnEvent;
  
  // for saving/loading
  public EndSexSession() {
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
  
  public EndSexSession(final NPC npc, final Event<?> returnEvent) {
    this.npc = npc;
    this.returnEvent = returnEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.clearFG(); // TODO
  }
  
  @Override
  protected void doEvent() {
    setText(Text.TEXT_END_SEX_SESSION);
    
    final long score = SexSession.finish();
    final TextWrapper scoreTextWrapper = ResourceCache.get(Text.TEXT_YOUR_SCORE);
    scoreTextWrapper.addParameter(String.valueOf(score));
    addText(scoreTextWrapper);
    
    addOption(Key.OPTION_ENTER, Text.OPTION_END_SEX_SESSION, new NPCOverviewEvent(this.npc, this.returnEvent));
  }
}
