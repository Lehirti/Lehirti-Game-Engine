package org.lehirti.luckysurvivor.npc.hinata;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.TextAndImageKey;
import org.lehirti.engine.res.images.ImageKey;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.sex.Sex;
import org.lehirti.engine.state.BoolState;
import org.lehirti.engine.state.IntState;
import org.lehirti.engine.state.State;
import org.lehirti.engine.state.StringState;
import org.lehirti.luckysurvivor.npc.AbstractNPC;
import org.lehirti.luckysurvivor.sss.ReactionToSexAct;
import org.lehirti.luckysurvivor.sss.SexAct;
import org.lehirti.luckysurvivor.sss.SexToy;

public final class Hinata extends AbstractNPC {
  private static final long serialVersionUID = 1L;
  
  // BEGIN GENERATED BLOCK NPCCommon
  public static enum String implements StringState {
    NAME,
    HAIR_COLOR,
    SKINTONE,
    BREAST_SHAPE,
    // BEGIN MANUAL BLOCK String
    // END MANUAL BLOCK String
  }
  
  public static enum Int implements IntState {
    HEIGHT,
    BREAST_SIZE,
    // BEGIN MANUAL BLOCK Int
    ABSOLUTE_UPPER_PAIN_THRESHOLD,
    UPPER_PAIN_COMFORT_THRESHOLD,
    LOWER_PAIN_COMFORT_THRESHOLD,
    CURRENT_PAIN_LEVEL,
    CURRENT_LUST,
    DISPOSITION_TOWARDS_PC,
    AROUSAL,
    ORGASM_THRESHOLD,
    VIGOR,
    // END MANUAL BLOCK Int
  }
  
  public static enum Bool implements BoolState {
    // BEGIN MANUAL BLOCK Bool
    // END MANUAL BLOCK Bool
  }
  
  // END GENERATED BLOCK NPCCommon
  
  public static enum Image implements ImageKey {
    MAIN,
  }
  
  public static enum Text implements TextKey {
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
  
  @Override
  public ImageKey getImage() {
    return Image.MAIN;
  }
  
  @Override
  public StringState getName() {
    return String.NAME;
  }
  
  @Override
  public TextWrapper getNameTextWrapper() {
    final TextWrapper textWrapper = ResourceCache.get(CommonText.PARAMETER);
    textWrapper.addParameter(State.get(String.NAME));
    return textWrapper;
  }
  
  @Override
  public Sex getSex() {
    return Sex.FEMALE;
  }
  
  @Override
  public List<TextWrapper> getGeneralDescription() {
    final List<TextWrapper> generalDescription = new LinkedList<>();
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
    final List<TextWrapper> texts = new LinkedList<>();
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
    // TODO Auto-generated method stub
    
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
    return (int) State.get(Int.CURRENT_PAIN_LEVEL);
  }
  
  @Override
  public int getCurrentLust() {
    return (int) State.get(Int.CURRENT_LUST);
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
