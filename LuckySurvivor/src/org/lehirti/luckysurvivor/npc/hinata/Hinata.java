package org.lehirti.luckysurvivor.npc.hinata;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.luckysurvivor.npc.AbstractNPC;

public class Hinata extends AbstractNPC {
  private static final long serialVersionUID = 1L;
  
  public static enum Image implements ImageKey {
    MAIN;
  }
  
  public static enum Text implements TextKey {
    NAME,
    GENERAL_DESCRIPTION;
  }
  
  @Override
  public ImageKey getImage() {
    return Image.MAIN;
  }
  
  @Override
  public TextKey getName() {
    return Text.NAME;
  }
  
  @Override
  public List<TextWrapper> getGeneralDescription() {
    final List<TextWrapper> generalDescription = new LinkedList<TextWrapper>();
    generalDescription.add(ResourceCache.get(Text.GENERAL_DESCRIPTION));
    return generalDescription;
  }
  
  @Override
  public List<TextWrapper> getGoOgleDescription() {
    return Collections.emptyList();
  }
}
