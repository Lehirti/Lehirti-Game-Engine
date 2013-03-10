package lge.events;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import lge.events.Event.NullState;
import lge.gui.Key;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.state.State;


public class AlternativeTextEvent extends EventNode<NullState> {
  
  private TextKey[] texts;
  private Event<?> nextEvent;
  
  // for saving/loading
  public AlternativeTextEvent() {
    this(null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    final int size = in.readInt();
    this.texts = new TextKey[size];
    for (int i = 0; i < size; i++) {
      this.texts[i] = (TextKey) in.readObject();
    }
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeInt(this.texts.length);
    for (final TextKey text : this.texts) {
      out.writeObject(text);
    }
    out.writeObject(this.nextEvent);
  }
  
  public AlternativeTextEvent(final Event<?> nextEvent, final TextKey firstTextAlternative,
      final TextKey... otherTextAlternatives) {
    this.texts = new TextKey[otherTextAlternatives.length + 1];
    this.texts[0] = firstTextAlternative;
    for (int i = 0; i < otherTextAlternatives.length; i++) {
      this.texts[i + 1] = otherTextAlternatives[i];
    }
    this.nextEvent = nextEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.noChange();
  }
  
  @Override
  protected void doEvent() {
    setText(this.texts[State.DIE.nextInt(this.texts.length)]);
    
    addOption(Key.OPTION_ENTER, CommonText.OPTION_NEXT, this.nextEvent);
  }
}
