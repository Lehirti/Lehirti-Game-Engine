package org.lehirti.luckysurvivor.npc;

import java.util.ArrayList;
import java.util.List;

import org.lehirti.engine.events.Event;
import org.lehirti.engine.events.NPCGoOgle;
import org.lehirti.engine.events.Option;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.state.NPC;

public abstract class AbstractNPC implements NPC {
  private static final long serialVersionUID = 1L;
  
  public enum Text implements TextKey {
    OPTION_GO_OGLE,
  }
  
  @Override
  public List<Option> getOverviewOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(11);
    // Examine
    options.add(new Option(Key.OPTION_A, Text.OPTION_GO_OGLE, new NPCGoOgle(this, returnEvent)));
    // Talk to:
    // Give Item:
    // Flirt with:
    // Inventory:
    return options;
  }
  
  @Override
  public List<Option> getGoOgleOptions(final Event<?> returnEvent) {
    final List<Option> options = new ArrayList<Option>(11);
    // body
    // bio
    // quests
    // likes
    return options;
  }
}
