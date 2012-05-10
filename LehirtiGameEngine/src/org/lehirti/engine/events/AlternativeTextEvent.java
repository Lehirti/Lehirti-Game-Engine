package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.StateObject;

public class AlternativeTextEvent extends EventNode<NullState> implements Externalizable {
  
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
    return ImgChange.nullChange();
  }
  
  @Override
  protected void doEvent() {
    setText(this.texts[StateObject.DIE.nextInt(this.texts.length)]);
    
    addOption(Key.OPTION_ENTER, CommonText.OPTION_NEXT, this.nextEvent);
  }
}
