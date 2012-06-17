package org.lehirti.luckysurvivor.sss;

import java.util.List;

import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.sex.Sex;

public interface SexParticipant {
  public Sex getSex();
  
  public List<SexAct> getAvailableSexActs();
  
  public void performSexAct(SexAct act, SexToy toy);
  
  public int getOrgasmThreshold();
  
  public boolean isOrgasming();
  
  public List<TextWrapper> getOrgasmingText(SexAct act, SexToy toy);
  
  public boolean isExhausted();
  
  public int getVigor();
  
  public int getArousal();
  
  public void setArousal(int newArousal);
}
