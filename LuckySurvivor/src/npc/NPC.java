package npc;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import lge.events.Event;
import lge.events.Option;
import lge.res.images.ImageKey;
import lge.res.text.TextParameterResolver;
import lge.res.text.TextWrapper;
import lge.state.StringState;

import sss.ReactionToSexAct;
import sss.SexAct;
import sss.SexParticipant;
import sss.SexToy;

public interface NPC extends TextParameterResolver, SexParticipant, Serializable {
  public StringState getName();
  
  public TextWrapper getNameTextWrapper();
  
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
  
  public Option createOption(final SexAct act, final SexToy toyIfAlreadySelected, final Event<?> returnEvent);
  
  /**
   * when during sex the PC proposes to perform a certain sex act, this method will return the NPCs reaction to the
   * proposition.
   * 
   * @param act
   * @param toy
   *          only required, if act requires a sex toy; else ignored
   * @return [-100; 100]
   */
  public Set<ReactionToSexAct> getReactionToPropositionOf(SexAct act, SexToy toy);
  
  public int getReluctanceToPerformAct(SexAct act, SexToy toy);
  
  public int getPainDelta(SexAct act, SexToy toy);
  
  public int getDispositionTowardsPC();
  
  public int getCurrentPainLevel();
  
  public int getLowerPainComfortThreshold();
  
  public int getUpperPainComfortThreshold();
  
  public int getAbsoluteUpperPainThreshold();
  
  public int getCurrentLust();
  
  /**
   * image of npc reacting to the prospect of the given sex act being performed
   * 
   * @param act
   * @param toy
   * @return
   */
  public ImageKey getReactionImage(SexAct act, SexToy toy);
  
  /**
   * text of npc reacting to the prospect of the given sex act being performed
   * 
   * @param act
   * @param toy
   * @return
   */
  public List<TextWrapper> getReactionText(SexAct act, SexToy toy);
  
  /**
   * player options after proposing given sex act to npc
   * 
   * @param act
   * @param toy
   * @return
   */
  public List<Option> getReactionOptions(SexAct act, SexToy toy, Event<?> returnEvent);
  
  public ImageKey getSexActPerformedImage(SexAct act, SexToy toy);
  
  public List<TextWrapper> getSexActPerformedText(SexAct act, SexToy toy);
  
  public List<Option> getSexActPerformedOptions(SexAct act, SexToy toy, Event<?> returnEvent);
  
  public ImageKey getOrgasmingImage(SexAct act, SexToy toy);
  
}
