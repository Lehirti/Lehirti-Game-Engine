package org.lehirti.luckysurvivor.sss;

import static org.lehirti.engine.sex.SexFeature.*;

import java.util.EnumSet;
import java.util.Set;

import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.sex.Sex;
import org.lehirti.engine.sex.SexFeature;

public enum SexAct implements TextKey {
  FUCK_PUSSY(COCK, PUSSY),
  GET_PUSSY_FUCKED(PUSSY, COCK),
  
  FUCK_ANAL(COCK, ANY),
  GET_FUCKED_ANALLY(ANY, COCK),
  
  GET_TITJOB(COCK, BREASTS),
  GIVE_TITJOB(BREASTS, COCK),
  
  GET_BLOWJOB(COCK, ANY),
  GIVE_BLOWJOB(ANY, COCK);
  
  public final SexFeature pcHas;
  public final SexFeature npcHas;
  
  private SexAct(final SexFeature pcHas, final SexFeature npcHas) {
    this.pcHas = pcHas;
    this.npcHas = npcHas;
  }
  
  public static Set<SexAct> getPhysicallyPossible(final Sex pc, final Sex npc) {
    final Set<SexAct> physicallyPossibleSexActs = EnumSet.noneOf(SexAct.class);
    for (final SexAct act : values()) {
      if (pc.has(act.pcHas) && npc.has(act.npcHas)) {
        physicallyPossibleSexActs.add(act);
      }
    }
    return physicallyPossibleSexActs;
  }
}
