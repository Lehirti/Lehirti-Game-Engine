package org.lehirti.luckysurvivor.sss;

import static org.lehirti.engine.sex.SexFeature.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.lehirti.engine.res.TextAndImageKey;
import org.lehirti.engine.sex.Sex;
import org.lehirti.engine.sex.SexFeature;

public enum SexAct implements TextAndImageKey {
  FUCK_PUSSY(COCK, 10, PUSSY, 10, 500),
  GET_PUSSY_FUCKED(PUSSY, 10, COCK, 10, 500),
  
  INSERT_TOY_INTO_PUSSY(ANY, 10, PUSSY, 10, 200, SexToyCategory.PUSSY),
  
  FUCK_ANAL(COCK, 10, ASS, 10, 500),
  GET_FUCKED_ANALLY(ASS, 10, COCK, 10, 500),
  
  GET_TITJOB(COCK, 10, BREASTS, 10, 400),
  GIVE_TITJOB(BREASTS, 10, COCK, 10, 400),
  
  GET_BLOWJOB(COCK, 10, MOUTH, 10, 500),
  GIVE_BLOWJOB(MOUTH, 10, COCK, 10, 500);
  
  public final SexFeature participant1;
  public final int exhaustion1;
  public final SexFeature participant2;
  public final int exhaustion2;
  public final int timeDDhhmmss;
  public final SexToyCategory requiredSexToy;
  
  private SexAct(final SexFeature participant1, final int exhaustion1, final SexFeature participant2,
      final int exhaustion2, final int timeDDhhmmss, final SexToyCategory requiredSexToy) {
    this.participant1 = participant1;
    this.exhaustion1 = exhaustion1;
    this.participant2 = participant2;
    this.exhaustion2 = exhaustion2;
    this.timeDDhhmmss = timeDDhhmmss;
    this.requiredSexToy = requiredSexToy;
  }
  
  private SexAct(final SexFeature participant1, final int exhaustion1, final SexFeature participant2,
      final int exhaustion2, final int timeDDhhmmss) {
    this(participant1, exhaustion1, participant2, exhaustion2, timeDDhhmmss, SexToyCategory.NONE);
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
