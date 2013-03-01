package org.lehirti.engine.events;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.BoolState;

public class SetFlagEvent extends EventNode<NullState> {
  
  private BoolState flag;
  private ImageKey image;
  private TextKey text;
  private Event<?> nextEvent;
  
  // for saving/loading
  public SetFlagEvent() {
    this(null, null, null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.flag = (BoolState) in.readObject();
    this.image = (ImageKey) in.readObject();
    this.text = (TextKey) in.readObject();
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.flag);
    out.writeObject(this.image);
    out.writeObject(this.text);
    out.writeObject(this.nextEvent);
  }
  
  public SetFlagEvent(final BoolState flag, final ImageKey image, final TextKey text, final Event<?> nextEvent) {
    this.flag = flag;
    this.image = image;
    this.text = text;
    this.nextEvent = nextEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(this.image);
  }
  
  @Override
  protected void doEvent() {
    set(this.flag, true);
    setText(this.text);
    
    addOption(Key.OPTION_ENTER, CommonText.OPTION_NEXT, this.nextEvent);
  }
}
