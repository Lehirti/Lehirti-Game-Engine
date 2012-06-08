package org.lehirti.luckysurvivor.npc;

import java.io.Serializable;
import java.util.List;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Option;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.luckysurvivor.sss.SexAct;
import org.lehirti.luckysurvivor.sss.SexParticipant;
import org.lehirti.luckysurvivor.sss.SexToy;

public interface NPC extends SexParticipant, Serializable {
  public TextKey getName();
  
  /**
   * @return the regular "meet the npc" image.
   */
  public ImageKey getImage();
  
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
  
  /**
   * when during sex the PC proposes to perform a certain sex act, this method will return the NPCs reaction to the
   * proposition.
   * 
   * @param act
   * @param toy
   *          only required, if act requires a sex toy; else ignored
   * @return [-100; 100]
   */
  public int getDispositionTo(SexAct act, SexToy toy);
  
  public ImageKey getReactionImage(SexAct proposedSexAct, SexToy toy);
  
  public List<TextWrapper> getReactionText(SexAct act, SexToy toy);
  
  public List<Option> getReactionOptions(SexAct act, SexToy toy, Event<?> returnEvent);
}
