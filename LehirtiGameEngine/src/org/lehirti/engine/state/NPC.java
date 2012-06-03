package org.lehirti.engine.state;

import java.io.Serializable;
import java.util.List;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Option;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;

public interface NPC extends Serializable {
  public TextKey getName();
  
  public ImageKey getImage();
  
  public List<TextWrapper> getGeneralDescription();
  
  public List<Option> getOverviewOptions(Event<?> returnEvent);
  
  public List<TextWrapper> getGoOgleDescription();
  
  public List<Option> getGoOgleOptions(Event<?> returnEvent);
}
