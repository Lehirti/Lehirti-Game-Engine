package org.lehirti.luckysurvivor.pc;

import java.util.Collections;
import java.util.List;

import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.sex.Sex;
import org.lehirti.engine.state.IntState;
import org.lehirti.engine.state.State;
import org.lehirti.engine.state.StringState;
import org.lehirti.luckysurvivor.npc.tifa.Tifa.ArousalFromPerformingSexAct;
import org.lehirti.luckysurvivor.sss.SexAct;
import org.lehirti.luckysurvivor.sss.SexParticipant;
import org.lehirti.luckysurvivor.sss.SexSession;
import org.lehirti.luckysurvivor.sss.SexToy;

public final class PC implements SexParticipant {
  public static enum String implements StringState {
    NAME;
  }
  
  public static enum Int implements IntState {
    AROUSAL,
    VIGOR,
    STAMINA,
    
    ORGASM_THESHOLD;
  }
  
  public static final PC PLAYER = new PC();
  
  @Override
  public Sex getSex() {
    return Sex.MALE;
  }
  
  @Override
  public List<SexAct> getAvailableSexActs() {
    return Collections.emptyList(); // TODO later, if/when the PC "is being sexed"
  }
  
  @Override
  public boolean isOrgasming() {
    return getArousal() > getOrgasmThreshold();
  }
  
  @Override
  public boolean isExhausted() {
    return getVigor() < 0;
  }
  
  @Override
  public int getArousal() {
    return (int) State.get(Int.AROUSAL);
  }
  
  @Override
  public void setArousal(final int newArousal) {
    State.set(Int.AROUSAL, newArousal);
  }
  
  @Override
  public int getOrgasmThreshold() {
    return (int) State.get(Int.ORGASM_THESHOLD);
  }
  
  @Override
  public int getVigor() {
    return (int) State.get(Int.VIGOR);
  }
  
  @Override
  public List<TextWrapper> getOrgasmingText(final SexAct act, final SexToy toy) {
    return Collections.emptyList(); // TODO
  }
  
  @Override
  public void performSexAct(final SexAct act, final SexToy toy) {
    
    // vigor depending on stamina, vigor decreases by 20-120% of base exhaustion for sex act
    State.change(Int.VIGOR, -(int) (act.exhaustion1 * (120 - State.get(Int.STAMINA)) / 100.0D));
    
    // TODO pain
    
    // arousal
    final long arousal = getArousal(act, toy);
    State.change(Int.AROUSAL, arousal);
    
    SexSession.getCurrent().updatePCPoints(arousal);
    
    // TODO orgasm check
  }
  
  private long getArousal(final SexAct act, final SexToy toy) {
    final long baseArousal = State.get(ArousalFromPerformingSexAct.valueOf(act.name()));
    switch (act.participant1) {
    case COCK: // TODO
    case PUSSY: // TODO
    case BREASTS: // TODO
    case MOUTH: // TODO
    case ASS: // TODO
    }
    return baseArousal;
  }
}
