package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.TextAndImageKeyWithFlag;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.state.StateObject;

public class AlternativeOneTimeEvents extends EventNode<NullState> implements Externalizable {
  
  private TextAndImageKeyWithFlag selectedEvent;
  private Event<?> nextEvent;
  
  // for saving/loading
  public AlternativeOneTimeEvents() {
    this(null, null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.selectedEvent = (TextAndImageKeyWithFlag) in.readObject();
    this.nextEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.selectedEvent);
    out.writeObject(this.nextEvent);
  }
  
  public AlternativeOneTimeEvents(final Event<?> nextEvent, final TextAndImageKeyWithFlag defaultEvent,
      final TextAndImageKeyWithFlag... oneTimeRandomEvents) {
    final List<TextAndImageKeyWithFlag> availableRandomEvents = new LinkedList<TextAndImageKeyWithFlag>();
    for (final TextAndImageKeyWithFlag oneRandomEvent : oneTimeRandomEvents) {
      if (is(oneRandomEvent)) {
        // this event has already been displayed
      } else {
        availableRandomEvents.add(oneRandomEvent);
      }
    }
    if (availableRandomEvents.isEmpty()) {
      this.selectedEvent = defaultEvent;
    } else {
      this.selectedEvent = availableRandomEvents.get(StateObject.DIE.nextInt(availableRandomEvents.size()));
    }
    this.nextEvent = nextEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(this.selectedEvent);
  }
  
  @Override
  protected void doEvent() {
    setText(this.selectedEvent);
    
    set(this.selectedEvent, true); // this one-time event has been displayed
    
    addOption(Key.OPTION_ENTER, CommonText.OPTION_NEXT, this.nextEvent);
  }
}
