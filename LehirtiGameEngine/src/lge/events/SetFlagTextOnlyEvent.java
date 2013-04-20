package lge.events;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.BoolState;

public class SetFlagTextOnlyEvent extends EventNode<NullState> {
  
  private BoolState flag;
  private boolean value;
  private Key key;
  private TextKey text;
  private Event<?> nextEvent;
  
  // for saving/loading
  public SetFlagTextOnlyEvent() {
    this(null, null, null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.flag = (BoolState) in.readObject();
    this.value = in.readBoolean();
    this.key = Key.read(in);
    this.text = (TextKey) in.readObject();
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.flag);
    out.writeBoolean(this.value);
    this.key.write(out);
    out.writeObject(this.text);
    out.writeObject(this.nextEvent);
  }
  
  public SetFlagTextOnlyEvent(final BoolState flag, final boolean value, final Key key, final TextKey text,
      final Event<?> nextEvent) {
    this.flag = flag;
    this.value = value;
    this.key = key;
    this.text = text;
    this.nextEvent = nextEvent;
  }
  
  public SetFlagTextOnlyEvent(final BoolState flag, final Key key, final TextKey text, final Event<?> nextEvent) {
    this(flag, true, key, text, nextEvent);
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    set(this.flag, this.value);
    setText(this.text);
    
    addOption(this.key, CommonText.OPTION_NEXT, this.nextEvent);
  }
}
