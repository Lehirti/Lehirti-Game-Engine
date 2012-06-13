package org.lehirti.luckysurvivor.npc.tifa;

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
import org.lehirti.engine.state.StateObject;
import org.lehirti.luckysurvivor.npc.AbstractNPC;
import org.lehirti.luckysurvivor.sss.ReactionToSexAct;
import org.lehirti.luckysurvivor.sss.SexAct;
import org.lehirti.luckysurvivor.sss.SexToy;

public class Tifa extends AbstractNPC {
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
    ;
  }
  
  private static enum Int implements IntState {
    ABSOLUTE_UPPER_PAIN_THRESHOLD,
    UPPER_PAIN_COMFORT_THRESHOLD,
    LOWER_PAIN_COMFORT_THRESHOLD,
    CURRENT_PAIN_LEVEL,
    CURRENT_LUST,
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
  public List<TextWrapper> getSexActPerformedText(final SexAct act, final SexToy toy) {
    return Collections.emptyList();
  }
  
  @Override
  public void updateStateAfterSexAct(final SexAct act, final SexToy toy) {
    // TODO Auto-generated method stub
  }
  
  @Override
  public int getAbsoluteUpperPainThreshold() {
    return (int) StateObject.get(Int.ABSOLUTE_UPPER_PAIN_THRESHOLD);
  }
  
  @Override
  public int getUpperPainComfortThreshold() {
    return (int) StateObject.get(Int.UPPER_PAIN_COMFORT_THRESHOLD);
  }
  
  @Override
  public int getLowerPainComfortThreshold() {
    return (int) StateObject.get(Int.LOWER_PAIN_COMFORT_THRESHOLD);
  }
  
  @Override
  public int getCurrentPainLevel() {
    return (int) StateObject.get(Int.CURRENT_PAIN_LEVEL);
  }
  
  @Override
  public int getCurrentLust() {
    return (int) StateObject.get(Int.CURRENT_LUST);
  }
  
  @Override
  public int getDispositionTowardsPC() {
    return (int) StateObject.get(Int.DISPOSITION_TOWARDS_PC);
  }
  
  @Override
  public int getPainDelta(final SexAct act, final SexToy toy) {
    return 0; // TODO
  }
  
  @Override
  public int getReluctanceToPerformAct(final SexAct act, final SexToy toy) {
    final ReluctanceToPerformSexAct reluctance = ReluctanceToPerformSexAct.valueOf(act.name());
    return (int) StateObject.get(reluctance);
  }
}
