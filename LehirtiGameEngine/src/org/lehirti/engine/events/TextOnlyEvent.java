package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class TextOnlyEvent extends EventNode<NullState> implements Externalizable {
  
  private TextKey text;
  private Event<?> nextEvent;
  
  // for saving/loading
  public TextOnlyEvent() {
    this(null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.text = (TextKey) in.readObject();
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.text);
    out.writeObject(this.nextEvent);
  }
  
  public TextOnlyEvent(final TextKey text, final Event<?> nextEvent) {
    this.text = text;
    this.nextEvent = nextEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.nullChange();
  }
  
  @Override
  protected void doEvent() {
    setText(this.text);
    
    addOption(CommonText.OPTION_NEXT, this.nextEvent);
  }
}
