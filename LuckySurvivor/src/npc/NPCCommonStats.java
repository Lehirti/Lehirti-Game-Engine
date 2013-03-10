package npc;

import lge.state.AbstractState;
import lge.state.IntState;
import lge.state.StringState;

/**
 * here you can define stats that all NPCs that are common to all NPCs.
 */
public enum NPCCommonStats {
  NAME(StringState.class),
  HAIR_COLOR(StringState.class),
  HEIGHT(IntState.class),
  SKINTONE(StringState.class),
  BREAST_SIZE(IntState.class),
  BREAST_SHAPE(StringState.class), ;
  
  // currently possible values are StringState, IntState, BoolState, can be easily extended to other types, if needed
  public final Class<? extends AbstractState> type;
  
  private NPCCommonStats(final Class<? extends AbstractState> type) {
    this.type = type;
  }
}
