package org.lehirti.luckysurvivor.sss;

import static org.lehirti.engine.sex.SexFeature.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.lehirti.engine.res.TextAndImageKey;
import org.lehirti.engine.sex.Sex;
import org.lehirti.engine.sex.SexFeature;

public enum SexAct implements TextAndImageKey {
  FUCK_PUSSY(COCK, PUSSY),
  GET_PUSSY_FUCKED(PUSSY, COCK),
  
  INSERT_TOY_INTO_PUSSY(ANY, PUSSY, SexToyCategory.PUSSY),
  
  FUCK_ANAL(COCK, ANY),
  GET_FUCKED_ANALLY(ANY, COCK),
  
  GET_TITJOB(COCK, BREASTS),
  GIVE_TITJOB(BREASTS, COCK),
  
  GET_BLOWJOB(COCK, ANY),
  GIVE_BLOWJOB(ANY, COCK);
  
  public final SexFeature participant1;
  public final SexFeature participant2;
  public final SexToyCategory requiredSexToy;
  
  private SexAct(final SexFeature participant1, final SexFeature participant2, final SexToyCategory requiredSexToy) {
    this.participant1 = participant1;
    this.participant2 = participant2;
    this.requiredSexToy = requiredSexToy;
  }
  
  private SexAct(final SexFeature participant1, final SexFeature participant2) {
    this(participant1, participant2, SexToyCategory.NONE);
  }
  
  public static Set<SexAct> getPhysicallyPossible(final Sex participantA, final Sex participantB) {
    final Set<SexAct> physicallyPossibleSexActs = EnumSet.noneOf(SexAct.class);
    for (final SexAct act : values()) {
      if (participantA.has(act.participant1) && participantB.has(act.participant2)) {
        physicallyPossibleSexActs.add(act);
      }
    }
    return physicallyPossibleSexActs;
  }
  
  public SexToyCategory getRequiredSexToy() {
    return this.requiredSexToy;
  }
  
  public int getSelectedIndex(final List<SexAct> availableSexActs) {
    for (int i = 0; i < availableSexActs.size(); i++) {
      if (availableSexActs.get(i) == this) {
        return i;
      }
    }
    return -1;
  }
}
