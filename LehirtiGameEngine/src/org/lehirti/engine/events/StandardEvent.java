package org.lehirti.engine.events;

import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;

public class StandardEvent extends EventNode {
  private final ImageKey image;
  private final TextKey text;
  private final Event nextEvent;
  
  // for saving/loading
  public StandardEvent() {
    this(null, null, null);
  }
  
  public StandardEvent(final ImageKey image, final Event nextEvent) {
    this(image, image, nextEvent);
  }
  
  public StandardEvent(final ImageKey image, final TextKey text, final Event nextEvent) {
    this.image = image;
    this.text = text;
    this.nextEvent = nextEvent;
  }
  
  @Override
  protected void doEvent() {
    setImage(this.image);
    setText(this.text);
    
    addOption(CommonText.OPTION_NEXT, this.nextEvent);
  }
}
