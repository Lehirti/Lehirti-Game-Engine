package org.lehirti.luckysurvivor.sss;

import java.util.List;

import org.lehirti.engine.sex.Sex;

public interface SexParticipant {
  public Sex getSex();
  
  public List<SexAct> getAvailableSexActs();
}
