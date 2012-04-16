package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class StandardEvent extends EventNode<NullState> implements Externalizable {
  
  private ImageKey image;
  private TextKey text;
  private Event<?> nextEvent;
  
  // for saving/loading
  public StandardEvent() {
    this(null, null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.image = (ImageKey) in.readObject();
    this.text = (TextKey) in.readObject();
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.image);
    out.writeObject(this.text);
    out.writeObject(this.nextEvent);
  }
  
  public StandardEvent(final ImageKey image, final Event<?> nextEvent) {
    this(image, image, nextEvent);
  }
  
  public StandardEvent(final ImageKey image, final TextKey text, final Event<?> nextEvent) {
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
    setImage(this.image);
    setText(this.text);
    
    addOption(CommonText.OPTION_NEXT, this.nextEvent);
  }
}
