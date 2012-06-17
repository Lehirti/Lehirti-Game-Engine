package org.lehirti.luckysurvivor.sss;

import org.lehirti.engine.state.DateTime;
import org.lehirti.luckysurvivor.pc.PC;

public final class SexSession {
  private static SexSession INSTANCE = null;
  
  private final SexParticipant participant;
  private final int startTimeDDhhmmss;
  private long npcPoints = 0;
  private long pcPoints = 0;
  
  public SexSession(final SexParticipant participant) {
    this.participant = participant;
    this.startTimeDDhhmmss = DateTime.getDDhhmmss();
  }
  
  public static SexSession start(final SexParticipant participant) {
    INSTANCE = new SexSession(participant);
    return INSTANCE;
  }
  
  public static SexSession getCurrent() {
    return INSTANCE;
  }
  
  public static long finish() {
    final long score = (INSTANCE.npcPoints - INSTANCE.participant.getArousal())
        + (INSTANCE.pcPoints - PC.PLAYER.getArousal()) / 2;
    INSTANCE = null;
    return score;
  }
  
  public void updateNPCPoints(final long points) {
    this.npcPoints += points;
  }
  
  public void updatePCPoints(final long points) {
    this.pcPoints += points;
  }
}
