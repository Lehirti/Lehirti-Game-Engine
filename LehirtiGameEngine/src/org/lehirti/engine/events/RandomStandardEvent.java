package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.TextAndImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.state.State;

public class RandomStandardEvent extends EventNode<NullState> implements Externalizable {
  
  private Key key;
  private TextAndImageKey randomTxtAndImgs;
  private Event<?> nextEvent;
  
  // for saving/loading
  public RandomStandardEvent() {
    this(null, null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.key = (Key) in.readObject();
    this.randomTxtAndImgs = (TextAndImageKey) in.readObject();
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.key);
    out.writeObject(this.randomTxtAndImgs);
    out.writeObject(this.nextEvent);
  }
  
  public RandomStandardEvent(final Key key, final Event<?> nextEvent, final TextAndImageKey firstAlternative,
      final TextAndImageKey... otherAlternatives) {
    final TextAndImageKey[] txtAndImgs = new TextAndImageKey[otherAlternatives.length + 1];
    txtAndImgs[0] = firstAlternative;
    for (int i = 0; i < otherAlternatives.length; i++) {
      txtAndImgs[i + 1] = otherAlternatives[i];
    }
    this.randomTxtAndImgs = txtAndImgs[State.DIE.nextInt(txtAndImgs.length)];
    this.nextEvent = nextEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(this.randomTxtAndImgs);
  }
  
  @Override
  protected void doEvent() {
    setText(this.randomTxtAndImgs);
    
    addOption(this.key, CommonText.OPTION_NEXT, this.nextEvent);
  }
}
