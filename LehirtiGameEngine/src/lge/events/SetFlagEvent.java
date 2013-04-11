package lge.events;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImageKey;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.BoolState;

public class SetFlagEvent extends EventNode<NullState> {
  
  private BoolState flag;
  private Key key;
  private ImageKey image;
  private TextKey text;
  private Event<?> nextEvent;
  
  // for saving/loading
  public SetFlagEvent() {
    this(null, null, null, null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.flag = (BoolState) in.readObject();
    this.key = (Key) in.readObject();
    this.image = (ImageKey) in.readObject();
    this.text = (TextKey) in.readObject();
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.flag);
    out.writeObject(this.key);
    out.writeObject(this.image);
    out.writeObject(this.text);
    out.writeObject(this.nextEvent);
  }
  
  public SetFlagEvent(final BoolState flag, final ImageKey image, final TextKey text, final Event<?> nextEvent) {
    this(flag, Key.OPTION_ENTER, image, text, nextEvent);
  }
  
  public SetFlagEvent(final BoolState flag, final Key key, final ImageKey image, final TextKey text,
      final Event<?> nextEvent) {
    this.flag = flag;
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
    set(this.flag, true);
    setText(this.text);
    
    addOption(this.key, CommonText.OPTION_NEXT, this.nextEvent);
  }
}
