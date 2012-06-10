package org.lehirti.luckysurvivor.pc;

import java.util.Collections;
import java.util.List;

import org.lehirti.engine.sex.Sex;
import org.lehirti.luckysurvivor.sss.SexAct;
import org.lehirti.luckysurvivor.sss.SexParticipant;

public final class PC implements SexParticipant {
  
  @Override
  public Sex getSex() {
    return Sex.MALE;
  }
  
  @Override
  public List<SexAct> getAvailableSexActs() {
    return Collections.emptyList(); // TODO later, if/when the PC "is being sexed"
  }
}
