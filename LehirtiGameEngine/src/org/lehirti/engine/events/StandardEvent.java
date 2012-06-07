package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class StandardEvent extends EventNode<NullState> implements Externalizable {
  
  private Key key;
  private ImageKey image;
  private TextKey text;
  private Event<?> nextEvent;
  
  // for saving/loading
  public StandardEvent() {
    this(null, null, null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.key = (Key) in.readObject();
    this.image = (ImageKey) in.readObject();
    this.text = (TextKey) in.readObject();
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.key);
    out.writeObject(this.image);
    out.writeObject(this.text);
    out.writeObject(this.nextEvent);
  }
  
  public StandardEvent(final Key key, final ImageKey image, final TextKey text, final Event<?> nextEvent) {
    this.key = key;
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
    setText(this.text);
    
    addOption(this.key, CommonText.OPTION_NEXT, this.nextEvent);
  }
}
