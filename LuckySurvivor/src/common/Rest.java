package common;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lge.events.Event;
import lge.events.Event.NullState;
import lge.events.EventNode;
import lge.gui.Key;
import lge.res.images.ImageKey;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.TimeInterval;

public class Rest extends EventNode<NullState> {
  @Nullable
  private Key key;
  @Nullable
  private TextKey text;
  @Nullable
  private ImageKey image;
  @Nonnull
  private Event<?> nextEvent;
  
  /**
   * for loading/saving
   */
  public Rest() {
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.key = Key.read(in);
    this.text = TextKey.IO.read(in);
    this.image = ImageKey.IO.read(in);
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    this.key.write(out);
    TextKey.IO.write(this.text, out);
    ImageKey.IO.write(this.image, out);
    out.writeObject(this.nextEvent);
  }
  
  public Rest(final @Nullable Key key, final @Nullable TextKey text, final @Nullable ImageKey image,
      final @Nonnull Event<?> nextEvent) {
    this.key = key;
    this.text = text;
    this.image = image;
    this.nextEvent = nextEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    if (this.image != null) {
      return ImgChange.setFG(this.image);
    }
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    setText(this.text);
    
    addOption(this.key, CommonText.OPTION_NEXT, this.nextEvent);
  }
  
  @Override
  public TimeInterval getRequiredTimeInterval() {
    // advance time to the next 8:00:00
    return TimeInterval.advanceTo(80000);
  }
}
