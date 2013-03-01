package org.lehirti.luckysurvivor.cliffwest;

import java.util.LinkedList;
import java.util.List;

import org.lehirti.luckysurvivor.npc.NPC;
import org.lehirti.luckysurvivor.npc.hinata.Hinata;
import org.lehirti.luckysurvivor.npc.tifa.Tifa;

public class CliffWestNPCs {
  /*
   * TODO there is no Jordan NPC, yet, so we're just displaying the same NPCs as at the crash site, but once there is a
   * Jordan NPC ... well looking at the lines below it's obvious what to change to make Jordan appear in the list
   * instead of Tifa and Hinata
   */
  public static List<NPC> getNPCs() {
    final List<NPC> list = new LinkedList<>();
    list.add(new Tifa());
    list.add(new Hinata());
    return list;
  }
}
