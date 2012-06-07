package org.lehirti.luckysurvivor.pc;

import org.lehirti.engine.sex.Sex;
import org.lehirti.luckysurvivor.sss.SexParticipant;

public final class PC implements SexParticipant {
  
  @Override
  public Sex getSex() {
    return Sex.MALE;
  }
}
