package lge.events;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;

public class TextOnlyEvent extends EventNode<NullState> {
  private static final long serialVersionUID = 1L;
  
  private Key key;
  private TextKey text;
  private Event<?> nextEvent;
  
  // for saving/loading
  public TextOnlyEvent() {
    this(null, null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.key = Key.read(in);
    this.text = (TextKey) in.readObject();
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    this.key.write(out);
    out.writeObject(this.text);
    out.writeObject(this.nextEvent);
  }
  
  public TextOnlyEvent(final Key key, final TextKey text, final Event<?> nextEvent) {
    this.key = key;
    this.text = text;
    this.nextEvent = nextEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    setText(this.text);
    
    addOption(this.key, CommonText.OPTION_NEXT, this.nextEvent);
  }
}
