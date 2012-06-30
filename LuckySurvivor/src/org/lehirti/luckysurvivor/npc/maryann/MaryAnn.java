package org.lehirti.luckysurvivor.npc.maryann;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.TextAndImageKey;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.sex.Sex;
import org.lehirti.engine.state.IntState;
import org.lehirti.engine.state.State;
import org.lehirti.luckysurvivor.npc.AbstractNPC;
import org.lehirti.luckysurvivor.sss.ReactionToSexAct;
import org.lehirti.luckysurvivor.sss.SexAct;
import org.lehirti.luckysurvivor.sss.SexSession;
import org.lehirti.luckysurvivor.sss.SexToy;

public class MaryAnn extends AbstractNPC {
  private static final long serialVersionUID = 1L;
  
  public static enum Image implements ImageKey {
    MAIN,
  }
  
  public static enum Text implements TextKey {
    NAME,
    GENERAL_DESCRIPTION;
  }
  
  public static enum ReactionToSexActImage implements TextAndImageKey {
    // BEGIN GENERATED BLOCK ReactionToSexAct
    REJECTED_REPULSED_BY_SEX_ACT_ON_PRINCIPLE,
    REJECTED_TOO_PAINFUL,
    REJECTED_ABSOLUTELY,
    REJECTED_STRONGLY,
    REJECTED,
    INDIFFERENT,
    ACCEPTED,
    ACCEPTED_LIKE,
    ACCEPTED_LOVE,
    // END GENERATED BLOCK ReactionToSexAct
  }
  
  public static enum SexActImage implements ImageKey {
    // BEGIN GENERATED BLOCK SexAct
    FUCK_PUSSY,
    GET_PUSSY_FUCKED,
    INSERT_TOY_INTO_PUSSY,
    FUCK_ANAL,
    GET_FUCKED_ANALLY,
    GET_TITJOB,
    GIVE_TITJOB,
    GET_BLOWJOB,
    GIVE_BLOWJOB,
    // END GENERATED BLOCK SexAct
  }
  
  public static enum OrgasmImage implements ImageKey {
    // BEGIN GENERATED BLOCK SexAct
    FUCK_PUSSY,
    GET_PUSSY_FUCKED,
    INSERT_TOY_INTO_PUSSY,
    FUCK_ANAL,
    GET_FUCKED_ANALLY,
    GET_TITJOB,
    GIVE_TITJOB,
    GET_BLOWJOB,
    GIVE_BLOWJOB,
    // END GENERATED BLOCK SexAct
  }
  
  public static enum ReluctanceToPerformSexAct implements IntState {
    // BEGIN GENERATED BLOCK SexAct
    FUCK_PUSSY,
    GET_PUSSY_FUCKED,
    INSERT_TOY_INTO_PUSSY,
    FUCK_ANAL,
    GET_FUCKED_ANALLY,
    GET_TITJOB,
    GIVE_TITJOB,
    GET_BLOWJOB,
    GIVE_BLOWJOB,
    // END GENERATED BLOCK SexAct
  }
  
  public static enum ArousalFromPerformingSexAct implements IntState {
    // BEGIN GENERATED BLOCK SexAct
    FUCK_PUSSY,
    GET_PUSSY_FUCKED,
    INSERT_TOY_INTO_PUSSY,
    FUCK_ANAL,
    GET_FUCKED_ANALLY,
    GET_TITJOB,
    GIVE_TITJOB,
    GET_BLOWJOB,
    GIVE_BLOWJOB,
    // END GENERATED BLOCK SexAct
  }
  
  private static enum Int implements IntState {
    ABSOLUTE_UPPER_PAIN_THRESHOLD,
    UPPER_PAIN_COMFORT_THRESHOLD,
    LOWER_PAIN_COMFORT_THRESHOLD,
    ORGASM_THRESHOLD,
    
    PAIN,
    LUST,
    AROUSAL,
    VIGOR,
    STAMINA,
    
    DISPOSITION_TOWARDS_PC;
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
  public Sex getSex() {
    return Sex.FEMALE;
  }
  
  @Override
  public List<TextWrapper> getGeneralDescription() {
    final List<TextWrapper> generalDescription = new LinkedList<TextWrapper>();
    generalDescription.add(ResourceCache.get(Text.GENERAL_DESCRIPTION));
    return generalDescription;
  }
  
  @Override
  public List<TextWrapper> getExamineDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getGoOgleDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getFlirtWithDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getGiveItemDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getInventoryDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getTalkToDescription() {
    return Collections.emptyList();
  }
  
  @Override
  public List<TextWrapper> getSexActsDescription() {
    return Collections.emptyList();
  }
  
  @Override
  protected TextAndImageKey getReactionImage(final ReactionToSexAct reaction) {
    return ReactionToSexActImage.valueOf(reaction.name());
  }
  
  @Override
  protected List<TextWrapper> getReactionText(final ReactionToSexAct reaction) {
    final TextKey text = getReactionImage(reaction);
    final TextWrapper textWrapper = ResourceCache.get(text);
    final List<TextWrapper> texts = new LinkedList<TextWrapper>();
    texts.add(textWrapper);
    return texts;
  }
  
  @Override
  public ImageKey getSexActPerformedImage(final SexAct act, final SexToy toy) {
    return SexActImage.valueOf(act.name());
  }
  
  @Override
  public ImageKey getOrgasmingImage(final SexAct act, final SexToy toy) {
    return OrgasmImage.valueOf(act.name());
  }
  
  @Override
  public List<TextWrapper> getSexActPerformedText(final SexAct act, final SexToy toy) {
    return Collections.emptyList();
  }
  
  @Override
  public void performSexAct(final SexAct act, final SexToy toy) {
    
    // vigor depending on stamina, vigor decreases by 20-120% of base exhaustion for sex act
    State.change(Int.VIGOR, -(int) (act.exhaustion2 * (120 - State.get(Int.STAMINA)) / 100.0D));
    
    // TODO pain
    
    // arousal
    final long arousal = getArousal(act, toy);
    State.change(Int.AROUSAL, arousal);
    
    SexSession.getCurrent().updateNPCPoints(arousal);
    
    // TODO orgasm check
  }
  
  private long getArousal(final SexAct act, final SexToy toy) {
    final long baseArousal = State.get(ArousalFromPerformingSexAct.valueOf(act.name()));
    switch (act.participant2) {
    case COCK: // TODO
    case PUSSY: // TODO
    case BREASTS: // TODO
    case MOUTH: // TODO
    case ASS: // TODO
    }
    return baseArousal;
  }
  
  @Override
  public int getAbsoluteUpperPainThreshold() {
    return (int) State.get(Int.ABSOLUTE_UPPER_PAIN_THRESHOLD);
  }
  
  @Override
  public int getUpperPainComfortThreshold() {
    return (int) State.get(Int.UPPER_PAIN_COMFORT_THRESHOLD);
  }
  
  @Override
  public int getLowerPainComfortThreshold() {
    return (int) State.get(Int.LOWER_PAIN_COMFORT_THRESHOLD);
  }
  
  @Override
  public int getCurrentPainLevel() {
    return (int) State.get(Int.PAIN);
  }
  
  @Override
  public int getCurrentLust() {
    return (int) State.get(Int.LUST);
  }
  
  @Override
  public int getDispositionTowardsPC() {
    return (int) State.get(Int.DISPOSITION_TOWARDS_PC);
  }
  
  @Override
  public int getPainDelta(final SexAct act, final SexToy toy) {
    return 0; // TODO
  }
  
  @Override
  public int getReluctanceToPerformAct(final SexAct act, final SexToy toy) {
    final ReluctanceToPerformSexAct reluctance = ReluctanceToPerformSexAct.valueOf(act.name());
    return (int) State.get(reluctance);
  }
  
  @Override
  public int getArousal() {
    return (int) State.get(Int.AROUSAL);
  }
  
  @Override
  public void setArousal(final int newArousal) {
    State.set(Int.AROUSAL, newArousal);
  }
  
  @Override
  public int getVigor() {
    return (int) State.get(Int.VIGOR);
  }
  
  @Override
  public int getOrgasmThreshold() {
    return (int) State.get(Int.ORGASM_THRESHOLD);
  }
}