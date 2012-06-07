package org.lehirti.luckysurvivor.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.Option;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.sex.Sex;
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
    
    OPTION_HAVE_SEX_WITH_HER
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
    options.add(new Option(Key.OPTION_ENTER, Text.OPTION_HAVE_SEX_WITH_HER, new NPCHaveSex(this, returnEvent)));
    return options;
  }
  
  @Override
  public List<Option> getInventoryOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(11);
    return options;
  }
  
  @Override
  public List<Option> getSexActsOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(11);
    final Set<SexAct> physicallyPossibleSexActs = SexAct.getPhysicallyPossible(Sex.MALE, getSex());
    for (final SexAct act : physicallyPossibleSexActs) {
      final SexToyCategory requiredSexToy = act.getRequiredSexToy();
      if (requiredSexToy == SexToyCategory.NONE) {
        if (getDispositionTo(act, null) > 0) {
          options.add(new Option(null, act, new PerformSexActEvent(this, act, null, returnEvent)));
        } else {
          options.add(new Option(null, act, new ProposeSexActEvent(this, act, null, returnEvent)));
        }
      } else {
        final List<SexToy> allAvailable = SexToy.getAllAvailable(requiredSexToy);
        if (allAvailable.size() > 1) {
          options.add(new Option(null, act, new SelectSexToyEvent(this, act, allAvailable, 0, returnEvent)));
        } else if (allAvailable.size() == 1) {
          final SexToy toy = allAvailable.get(0);
          if (getDispositionTo(act, toy) > 0) {
            options.add(new Option(null, act, new PerformSexActEvent(this, act, toy, returnEvent)));
          } else {
            options.add(new Option(null, act, new ProposeSexActEvent(this, act, toy, returnEvent)));
          }
        }
      }
    }
    return options;
  }
  
  @Override
  public int getDispositionTo(final SexAct act, final SexToy toy) {
    // TODO determine act disposition from NPC preference to act, NPC disposition to PC and NPC lust
    return 0;
  }
  
  public List<Option> getReactionOptions(final SexAct act, final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(12);
    options.add(new Option(Key.OPTION_LEAVE, CommonText.OPTION_BACK, new NPCHaveSex(this, returnEvent)));
    options.add(new Option(Key.OPTION_ENTER, CommonText.OPTION_DO_IT, null)); // TODO
    return options;
  }
}
