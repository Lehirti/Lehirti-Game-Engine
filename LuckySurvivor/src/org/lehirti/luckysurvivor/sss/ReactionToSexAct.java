package org.lehirti.luckysurvivor.sss;

import java.util.Set;

public enum ReactionToSexAct {
  REJECTED_REPULSED_BY_SEX_ACT_ON_PRINCIPLE,
  REJECTED_TOO_PAINFUL,
  REJECTED_ABSOLUTELY,
  REJECTED_STRONGLY,
  REJECTED,
  INDIFFERENT,
  ACCEPTED,
  ACCEPTED_LIKE,
  ACCEPTED_LOVE;
  
  /**
   * @param reactions
   * @return the reaction with the lowest ordinal in the set; null, if set is empty
   */
  public static ReactionToSexAct getEffective(final Set<ReactionToSexAct> reactions) {
    if (reactions.isEmpty()) {
      return null;
    }
    int lowestOrdinal = Integer.MAX_VALUE;
    for (final ReactionToSexAct reaction : reactions) {
      if (reaction.ordinal() < lowestOrdinal) {
        lowestOrdinal = reaction.ordinal();
      }
    }
    return values()[lowestOrdinal];
  }
}
