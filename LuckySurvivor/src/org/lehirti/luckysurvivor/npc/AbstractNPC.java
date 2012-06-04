package org.lehirti.luckysurvivor.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.NPCExamine;
import org.lehirti.engine.events.NPCFlirtWith;
import org.lehirti.engine.events.NPCGiveItem;
import org.lehirti.engine.events.NPCGoOgle;
import org.lehirti.engine.events.NPCHaveSex;
import org.lehirti.engine.events.NPCInventory;
import org.lehirti.engine.events.NPCTalkTo;
import org.lehirti.engine.events.Option;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.sex.Sex;
import org.lehirti.engine.state.NPC;
import org.lehirti.luckysurvivor.sss.SexAct;
import org.lehirti.luckysurvivor.sss.SexActEvent;

public abstract class AbstractNPC implements NPC {
  private static final long serialVersionUID = 1L;
  
  public enum Text implements TextKey {
    OPTION_EXAMINE,
    OPTION_GO_OGLE,
    OPTION_TALK_TO,
    OPTION_GIVE_ITEM,
    OPTION_FLIRT_WITH,
    OPTION_INVENTORY,
    
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
    // body
    // bio
    // quests
    // likes
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
      options.add(new Option(null, act, new SexActEvent(this, act, returnEvent)));
    }
    return options;
  }
}
