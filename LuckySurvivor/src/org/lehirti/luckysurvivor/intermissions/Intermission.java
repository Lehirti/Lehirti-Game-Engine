package org.lehirti.luckysurvivor.intermissions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.annotation.Nonnull;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.TextAndImageKey;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.state.State;

/**
 * When resting (using the "Rest" event) and the conditions are right (see IntermissionsHook), before the "Rest" event
 * this one will be shown. This Intermission event class itself will keep track of which intermission to show by use of
 * the event counter
 */
public class Intermission extends EventNode<NullState> {
  public static enum TxtImg implements TextAndImageKey {
    // if you want another intermission, just add another constant here
    INTERMISSION_1(2),
    INTERMISSION_2(3),
    INTERMISSION_3(4),
    INTERMISSION_4(5),
    INTERMISSION_5(10),
    INTERMISSION_6(14);
    
    public final int afterDays;
    
    private TxtImg(final int afterDays) {
      this.afterDays = afterDays;
    }
  }
  
  @Nonnull
  private Event<?> restEvent;
  
  private final TxtImg currentTxtImg;
  
  /**
   * for load/save
   */
  public Intermission() {
    this.currentTxtImg = TxtImg.values()[State.getEventCount(Intermission.class)];
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.restEvent = (Event<?>) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.restEvent);
  }
  
  public Intermission(final Event<?> restEvent) {
    this();
    this.restEvent = restEvent;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    return ImgChange.setFG(this.currentTxtImg);
  }
  
  @Override
  protected void doEvent() {
    setText(this.currentTxtImg);
    addOption(Key.OPTION_ENTER, CommonText.OPTION_NEXT, this.restEvent);
  }
}
