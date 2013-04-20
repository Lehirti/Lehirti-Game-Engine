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

public class StandardEvent extends EventNode<NullState> {
  
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
    this.key = Key.read(in);
    this.image = (ImageKey) in.readObject();
    this.text = (TextKey) in.readObject();
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    this.key.write(out);
    out.writeObject(this.image);
    out.writeObject(this.text);
    out.writeObject(this.nextEvent);
  }
  
  /*
   * here we are! the StandardEvent class only has this one (real) constructor (a method that has the same name as the
   * class and defines no explicit return value). here you see which parameters you need to create a proper
   * StandardEvent instance. in line 23 there is one other constructor, but this one is for loading/saving only. using
   * it in regular code would create a broken event.
   */
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
