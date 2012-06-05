package org.lehirti.luckysurvivor.npc;

import java.io.Serializable;
import java.util.List;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Option;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.sex.Sex;
import org.lehirti.luckysurvivor.sss.SexAct;

public interface NPC extends Serializable {
  public TextKey getName();
  
  public ImageKey getImage();
  
  public Sex getSex();
  
  public List<TextWrapper> getGeneralDescription();
  
  public List<Option> getOverviewOptions(Event<?> returnEvent);
  
  public List<TextWrapper> getExamineDescription();
  
  public List<Option> getExamineOptions(Event<?> returnEvent);
  
  public List<TextWrapper> getGoOgleDescription();
  
  public List<Option> getGoOgleOptions(Event<?> returnEvent);
  
  public List<TextWrapper> getTalkToDescription();
  
  public List<Option> getTalkToOptions(Event<?> returnEvent);
  
  public List<TextWrapper> getGiveItemDescription();
  
  public List<Option> getGiveItemOptions(Event<?> returnEvent);
  
  public List<TextWrapper> getFlirtWithDescription();
  
  public List<Option> getFlirtWithOptions(Event<?> returnEvent);
  
  public List<TextWrapper> getInventoryDescription();
  
  public List<Option> getInventoryOptions(Event<?> returnEvent);
  
  public List<TextWrapper> getSexActsDescription();
  
  public List<Option> getSexActsOptions(Event<?> returnEvent);
  
  public int getDispositionTo(SexAct act, boolean proposeItToNPC);
  
  public ImageKey getReactionImage(SexAct proposedSexAct, int npcDispositionToSexAct);
  
  public List<TextWrapper> getReactionText(SexAct act, int npcDispositionToSexAct);
}
