package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.BoolState;

public class SetFlagTextOnlyEvent extends EventNode<NullState> implements Externalizable {
  
  private BoolState flag;
  private TextKey text;
  private Event<?> nextEvent;
  
  // for saving/loading
  public SetFlagTextOnlyEvent() {
    this(null, null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.flag = (BoolState) in.readObject();
    this.text = (TextKey) in.readObject();
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.flag);
    out.writeObject(this.text);
    out.writeObject(this.nextEvent);
  }
  
  public SetFlagTextOnlyEvent(final BoolState flag, final TextKey text, final Event<?> nextEvent) {
    this.flag = flag;
    this.text = text;
    this.nextEvent = nextEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.nullChange();
  }
  
  @Override
  protected void doEvent() {
    set(this.flag, true);
    setText(this.text);
    
    addOption(Key.OPTION_ENTER, CommonText.OPTION_NEXT, this.nextEvent);
  }
}
