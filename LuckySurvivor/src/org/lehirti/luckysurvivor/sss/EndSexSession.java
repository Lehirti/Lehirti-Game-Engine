package org.lehirti.luckysurvivor.sss;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.luckysurvivor.npc.NPC;
import org.lehirti.luckysurvivor.npc.NPCOverviewEvent;

public class EndSexSession extends EventNode<NullState> implements Externalizable {
  
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
