package org.lehirti.luckysurvivor.npc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Option;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.luckysurvivor.pc.PC;
import org.lehirti.luckysurvivor.sss.PerformSexActEvent;
import org.lehirti.luckysurvivor.sss.ProposeSexActEvent;
import org.lehirti.luckysurvivor.sss.SelectSexToyEvent;
import org.lehirti.luckysurvivor.sss.SexAct;
import org.lehirti.luckysurvivor.sss.SexToy;
import org.lehirti.luckysurvivor.sss.SexToyCategory;

public abstract class AbstractNPC implements NPC {
  private static final long serialVersionUID = 1L;
  
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
  }
  
  @Override
  public List<Option> getOverviewOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(11);
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
    final List<Option> options = new ArrayList<Option>(11);
    options.add(new Option(Key.OPTION_WEST, Text.OPTION_BODY, new NPCExamine(this, returnEvent))); // TODO
    options.add(new Option(Key.OPTION_SOUTH, Text.OPTION_BIO, new NPCExamine(this, returnEvent))); // TODO
    options.add(new Option(Key.OPTION_EAST, Text.OPTION_QUESTS, new NPCExamine(this, returnEvent))); // TODO
    options.add(new Option(Key.OPTION_NORTH, Text.OPTION_LIKES, new NPCExamine(this, returnEvent))); // TODO
    return options;
  }
  
  @Override
  public List<Option> getGoOgleOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(11);
    return options;
  }
  
  @Override
  public List<Option> getTalkToOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(11);
    return options;
  }
  
  @Override
  public List<Option> getGiveItemOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(11);
    return options;
  }
  
  @Override
  public List<Option> getFlirtWithOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(11);
    options.add(new Option(Key.OPTION_ENTER, Text.OPTION_HAVE_SEX_WITH_HER, new NPCHaveSex(this, getAvailableSexActs(),
        0, returnEvent)));
    return options;
  }
  
  @Override
  public List<Option> getInventoryOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(11);
    return options;
  }
  
  @Override
  public List<SexAct> getAvailableSexActs() {
    final List<SexAct> availableSexActs = new LinkedList<SexAct>();
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
      if (getDispositionTo(act, null) > 0) {
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
        if (getDispositionTo(act, toy) > 0) {
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
  public int getDispositionTo(final SexAct act, final SexToy toy) {
    // TODO determine act disposition from NPC preference to act, NPC disposition to PC and NPC lust
    return 0;
  }
  
  @Override
  public List<Option> getReactionOptions(final SexAct act, final SexToy toy, final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(12);
    final List<SexAct> availableSexActs = getAvailableSexActs();
    final int selectedAct = act.getSelectedIndex(availableSexActs);
    options.add(new Option(Key.OPTION_LEAVE, Text.OPTION_CHANGE_SEX_ACT, new NPCHaveSex(this, availableSexActs,
        selectedAct, returnEvent)));
    options.add(new Option(Key.OPTION_ENTER, CommonText.OPTION_DO_IT, new PerformSexActEvent(this, act, toy,
        returnEvent)));
    return options;
  }
  
  @Override
  public List<Option> getSexActPerformedOptions(final SexAct act, final SexToy toy, final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(12);
    final List<SexAct> availableSexActs = getAvailableSexActs();
    final int selectedAct = act.getSelectedIndex(availableSexActs);
    options.add(new Option(Key.OPTION_LEAVE, Text.OPTION_CHANGE_SEX_ACT, new NPCHaveSex(this, availableSexActs,
        selectedAct, returnEvent)));
    final Option option = createOption(act, toy, returnEvent);
    if (option != null) {
      options.add(new Option(Key.OPTION_ENTER, Text.OPTION_REPEAT_SEX_ACT, option.event));
    }
    return options;
  }
}
