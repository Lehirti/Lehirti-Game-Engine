package npc.hinata;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lge.res.ResourceCache;
import lge.res.TextAndImageKey;
import lge.res.images.ImageKey;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.res.text.TextWrapper;
import lge.sex.Sex;
import lge.state.AbstractState;
import lge.state.BoolState;
import lge.state.IntState;
import lge.state.State;
import lge.state.StringState;

import npc.AbstractNPC;

import sss.ReactionToSexAct;
import sss.SexAct;
import sss.SexToy;

public final class Hinata extends AbstractNPC {
  
  // BEGIN GENERATED BLOCK NPCCommon
  public static enum Str implements StringState {
    NAME,
    HAIR_COLOR,
    SKINTONE,
    BREAST_SHAPE,
    // BEGIN MANUAL BLOCK Str
    // END MANUAL BLOCK Str
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
  
  private final static Map<String, AbstractState> STATE_BY_NAME_MAP = new LinkedHashMap<>();
  static {
    for (final AbstractState state : Str.values()) {
      if (STATE_BY_NAME_MAP.containsKey(state.name())) {
        throw new ThreadDeath();
      }
      STATE_BY_NAME_MAP.put(state.name(), state);
    }
    for (final AbstractState state : Int.values()) {
      if (STATE_BY_NAME_MAP.containsKey(state.name())) {
        throw new ThreadDeath();
      }
      STATE_BY_NAME_MAP.put(state.name(), state);
    }
    for (final AbstractState state : Bool.values()) {
      if (STATE_BY_NAME_MAP.containsKey(state.name())) {
        throw new ThreadDeath();
      }
      STATE_BY_NAME_MAP.put(state.name(), state);
    }
  }
  
  @Override
  public String resolveParameter(final String parameterSuffix) {
    final AbstractState abstractState = STATE_BY_NAME_MAP.get(parameterSuffix);
    if (abstractState == null) {
      return "[Hinata.resolveParameter(" + parameterSuffix + "): UNKNOWN]";
    }
    return State.get(abstractState);
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
    return Str.NAME;
  }
  
  @Override
  public TextWrapper getNameTextWrapper() {
    final TextWrapper textWrapper = ResourceCache.get(CommonText.PARAMETER);
    textWrapper.addParameter(State.get(Str.NAME));
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
    final TextWrapper textWrapper = ResourceCache.getNullable(text);
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
