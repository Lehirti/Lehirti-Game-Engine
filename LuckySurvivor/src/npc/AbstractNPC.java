package npc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lge.events.Event;
import lge.events.Option;
import lge.gui.Key;
import lge.res.ResourceCache;
import lge.res.images.ImageKey;
import lge.res.text.CommonText;
import lge.res.text.TextKey;
import lge.res.text.TextParameterResolutionException;
import lge.res.text.TextWrapper;
import lge.state.AbstractState;
import lge.state.IntState;
import lge.state.OrderedTextKeyResolver;
import lge.state.State;
import pc.PC;
import sss.EndSexSession;
import sss.Orgasm;
import sss.PerformSexActEvent;
import sss.ProposeSexActEvent;
import sss.ReactionToSexAct;
import sss.SelectSexAct;
import sss.SelectSexToyEvent;
import sss.SexAct;
import sss.SexToy;
import sss.SexToyCategory;
import text.BreastCupSize;
import text.BreastSize;
import text.UnderbustSize;

public abstract class AbstractNPC implements NPC {
  public static final long serialVersionUID = 1L; // yes, public!
  
  public enum Text implements TextKey {
    OPTION_EXAMINE,
    OPTION_GO_OGLE,
    OPTION_TALK_TO,
    OPTION_GIVE_ITEM,
    OPTION_FLIRT_WITH,
    OPTION_INVENTORY,
    
    OPTION_BODY,
    OPTION_BIO,
    OPTION_QUESTS,
    OPTION_LIKES,
    
    OPTION_HAVE_SEX_WITH_HER,
    OPTION_CHANGE_SEX_ACT,
    OPTION_REPEAT_SEX_ACT,
    OPTION_ORGASM,
    OPTION_END_SEX_SESSION,
  }
  
  @Override
  public List<Option> getOverviewOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<>(11);
    options.add(new Option(Key.OPTION_A, Text.OPTION_EXAMINE, new NPCExamine(this, returnEvent)));
    options.add(new Option(Key.OPTION_WEST, Text.OPTION_GO_OGLE, new NPCGoOgle(this, returnEvent)));
    options.add(new Option(Key.OPTION_SOUTH, Text.OPTION_TALK_TO, new NPCTalkTo(this, returnEvent)));
    options.add(new Option(Key.OPTION_EAST, Text.OPTION_GIVE_ITEM, new NPCGiveItem(this, returnEvent)));
    options.add(new Option(Key.OPTION_NORTH, Text.OPTION_FLIRT_WITH, new NPCFlirtWith(this, returnEvent)));
    options.add(new Option(Key.OPTION_Q, Text.OPTION_INVENTORY, new NPCInventory(this, returnEvent)));
    return options;
  }
  
  @Override
  public List<Option> getExamineOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<>(11);
    options.add(new Option(Key.OPTION_WEST, Text.OPTION_BODY, new NPCExamine(this, returnEvent))); // TODO
    options.add(new Option(Key.OPTION_SOUTH, Text.OPTION_BIO, new NPCExamine(this, returnEvent))); // TODO
    options.add(new Option(Key.OPTION_EAST, Text.OPTION_QUESTS, new NPCExamine(this, returnEvent))); // TODO
    options.add(new Option(Key.OPTION_NORTH, Text.OPTION_LIKES, new NPCExamine(this, returnEvent))); // TODO
    return options;
  }
  
  @Override
  public List<Option> getGoOgleOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<>(11);
    return options;
  }
  
  @Override
  public List<Option> getTalkToOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<>(11);
    return options;
  }
  
  @Override
  public List<Option> getGiveItemOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<>(11);
    return options;
  }
  
  @Override
  public List<Option> getFlirtWithOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<>(11);
    // options.add(new Option(Key.OPTION_ENTER, Text.OPTION_HAVE_SEX_WITH_HER, new StartSexSession(this, returnEvent)));
    return options;
  }
  
  @Override
  public List<Option> getInventoryOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<>(11);
    return options;
  }
  
  @Override
  public List<SexAct> getAvailableSexActs() {
    final List<SexAct> availableSexActs = new LinkedList<>();
    final Set<SexAct> physicallyPossibleSexActs = SexAct.getPhysicallyPossible(new PC().getSex(), getSex());
    for (final SexAct act : physicallyPossibleSexActs) {
      if (act.getRequiredSexToy() == SexToyCategory.NONE || !SexToy.getAllAvailable(act.getRequiredSexToy()).isEmpty()) {
        availableSexActs.add(act);
      }
    }
    return availableSexActs;
  }
  
  public Option createOption(final SexAct act, final SexToy toyIfAlreadySelected, final Event<?> returnEvent) {
    final SexToyCategory requiredSexToy = act.getRequiredSexToy();
    if (requiredSexToy == SexToyCategory.NONE) {
      if (ReactionToSexAct.getEffective(getReactionToPropositionOf(act, null)).ordinal() > ReactionToSexAct.INDIFFERENT
          .ordinal()) {
        return new Option(null, act, new PerformSexActEvent(this, act, null, returnEvent));
      } else {
        return new Option(null, act, new ProposeSexActEvent(this, act, null, returnEvent));
      }
    } else {
      SexToy toy = toyIfAlreadySelected;
      final List<SexToy> allAvailable = SexToy.getAllAvailable(requiredSexToy);
      if (toy == null && allAvailable.size() == 1) {
        toy = allAvailable.get(0);
      }
      if (toy != null) {
        if (ReactionToSexAct.getEffective(getReactionToPropositionOf(act, toy)).ordinal() > ReactionToSexAct.INDIFFERENT
            .ordinal()) {
          return new Option(null, act, new PerformSexActEvent(this, act, toy, returnEvent));
        } else {
          return new Option(null, act, new ProposeSexActEvent(this, act, toy, returnEvent));
        }
      } else if (allAvailable.size() > 1) {
        return new Option(null, act, new SelectSexToyEvent(this, act, allAvailable, 0, returnEvent));
      }
    }
    return null;
  }
  
  @Override
  public Set<ReactionToSexAct> getReactionToPropositionOf(final SexAct act, final SexToy toy) {
    final Set<ReactionToSexAct> reactions = EnumSet.noneOf(ReactionToSexAct.class);
    
    // check, if the act is unconditionally rejected
    final int reluctance = getReluctanceToPerformAct(act, toy);
    if (reluctance > 100) {
      reactions.add(ReactionToSexAct.REJECTED_REPULSED_BY_SEX_ACT_ON_PRINCIPLE);
    }
    
    // check, if the act is perceived as too painful
    final int expectedPainLevel = getPainDelta(act, toy) + getCurrentPainLevel();
    if (expectedPainLevel > getAbsoluteUpperPainThreshold()) {
      reactions.add(ReactionToSexAct.REJECTED_TOO_PAINFUL);
    }
    
    // check, if disposition towards PC and NPC lust overcome reluctance towards act and anticipated pain
    
    // general willingness
    int willingnessToPerformSexAct = getDispositionTowardsPC() - reluctance;
    
    // modified by pain
    if (expectedPainLevel > getUpperPainComfortThreshold()) {
      // it hurts too much
      willingnessToPerformSexAct -= (expectedPainLevel - getUpperPainComfortThreshold());
    }
    if (expectedPainLevel < getLowerPainComfortThreshold()) {
      // it's boring when you hardly feel anything
      willingnessToPerformSexAct -= (getLowerPainComfortThreshold() - expectedPainLevel);
    }
    
    // modified by lust
    willingnessToPerformSexAct += getCurrentLust() / 10;
    if (willingnessToPerformSexAct < -50) {
      reactions.add(ReactionToSexAct.REJECTED_ABSOLUTELY);
    } else if (willingnessToPerformSexAct < -30) {
      reactions.add(ReactionToSexAct.REJECTED_STRONGLY);
    } else if (willingnessToPerformSexAct < -10) {
      reactions.add(ReactionToSexAct.REJECTED);
    } else if (willingnessToPerformSexAct < 10) {
      reactions.add(ReactionToSexAct.INDIFFERENT);
    } else if (willingnessToPerformSexAct < 30) {
      reactions.add(ReactionToSexAct.ACCEPTED);
    } else if (willingnessToPerformSexAct < 50) {
      reactions.add(ReactionToSexAct.ACCEPTED_LIKE);
    } else {
      reactions.add(ReactionToSexAct.ACCEPTED_LOVE);
    }
    
    return reactions;
  }
  
  @Override
  public ImageKey getReactionImage(final SexAct act, final SexToy toy) {
    return getReactionImage(ReactionToSexAct.getEffective(getReactionToPropositionOf(act, toy)));
  }
  
  abstract protected ImageKey getReactionImage(final ReactionToSexAct reaction);
  
  @Override
  public List<TextWrapper> getReactionText(final SexAct act, final SexToy toy) {
    return getReactionText(ReactionToSexAct.getEffective(getReactionToPropositionOf(act, toy)));
  }
  
  abstract protected List<TextWrapper> getReactionText(final ReactionToSexAct reaction);
  
  @Override
  public List<Option> getReactionOptions(final SexAct act, final SexToy toy, final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<>(12);
    final List<SexAct> availableSexActs = getAvailableSexActs();
    final int selectedAct = act.getSelectedIndex(availableSexActs);
    options.add(new Option(Key.OPTION_LEAVE, Text.OPTION_CHANGE_SEX_ACT, new SelectSexAct(this, availableSexActs,
        selectedAct, returnEvent)));
    options.add(new Option(Key.OPTION_ENTER, CommonText.OPTION_DO_IT, new PerformSexActEvent(this, act, toy,
        returnEvent)));
    return options;
  }
  
  @Override
  public List<Option> getSexActPerformedOptions(final SexAct act, final SexToy toy, final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<>(12);
    if (isOrgasming() || PC.PLAYER.isOrgasming()) {
      options.add(new Option(Key.OPTION_ENTER, Text.OPTION_ORGASM, new Orgasm(this, act, toy, returnEvent)));
    } else if (isExhausted() || PC.PLAYER.isExhausted()) {
      options.add(new Option(Key.OPTION_ENTER, Text.OPTION_END_SEX_SESSION, new EndSexSession(this, returnEvent)));
    } else {
      final List<SexAct> availableSexActs = getAvailableSexActs();
      final int selectedAct = act.getSelectedIndex(availableSexActs);
      options.add(new Option(Key.OPTION_LEAVE, Text.OPTION_CHANGE_SEX_ACT, new SelectSexAct(this, availableSexActs,
          selectedAct, returnEvent)));
      final Option option = createOption(act, toy, returnEvent);
      if (option != null) {
        options.add(new Option(Key.OPTION_ENTER, Text.OPTION_REPEAT_SEX_ACT, option.event));
      }
    }
    return options;
  }
  
  @Override
  public boolean isExhausted() {
    return getVigor() < 0;
  }
  
  @Override
  public boolean isOrgasming() {
    return getArousal() > getOrgasmThreshold();
  }
  
  @Override
  public List<TextWrapper> getOrgasmingText(final SexAct act, final SexToy toy) {
    return Collections.emptyList(); // TODO
  }
  
  @Override
  public String getParameterPrefix() {
    return getClass().getSimpleName() + "#";
  }
  
  public String resolveParameter(final String parameter, final Map<String, AbstractState> stateMap)
      throws TextParameterResolutionException {
    final AbstractState abstractState = stateMap.get(parameter);
    
    // TODO temporary changes
    switch (parameter) {
    case "UNDERBUST_SIZE":
      final long underbustSizeInMM = State.get((IntState) abstractState);
      return ResourceCache.get(OrderedTextKeyResolver.resolve(underbustSizeInMM, UnderbustSize.values())).getValue();
    case "BREAST_SIZE":
      long breastSizeInMM = State.get((IntState) abstractState);
      return ResourceCache.get(OrderedTextKeyResolver.resolve(breastSizeInMM, BreastSize.values())).getValue();
    case "CUP":
      breastSizeInMM = State.get((IntState) abstractState);
      return ResourceCache.get(OrderedTextKeyResolver.resolve(breastSizeInMM, BreastCupSize.values())).getValue();
    case "BUST_MEASUREMENT":
      final IntState underbustSize = (IntState) stateMap.get(NPCCommonStats.UNDERBUST_SIZE.name());
      final IntState breastSize = (IntState) stateMap.get(NPCCommonStats.BREAST_SIZE.name());
      return String.valueOf(State.get(underbustSize) + State.get(breastSize)); // TODO change mm value
    }
    
    // if there are no special rules for this parameter, interpret it as a variable
    if (abstractState == null) {
      // parameter unknown; this is probably an error in the text
      throw new TextParameterResolutionException("[" + getClass().getSimpleName() + ".resolveParameter(" + parameter
          + "): UNKNOWN]");
    }
    return State.get(abstractState);
  }
}
