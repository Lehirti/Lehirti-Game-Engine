package org.lehirti.luckysurvivor.crashsite;

import java.util.LinkedList;
import java.util.List;

import org.lehirti.luckysurvivor.npc.NPC;
import org.lehirti.luckysurvivor.npc.hinata.Hinata;
import org.lehirti.luckysurvivor.npc.tifa.Tifa;
import org.lehirti.luckysurvivor.npc.maryann.MaryAnn;

public class CrashSiteNPCs {
  public static List<NPC> getNPCs() {
    final List<NPC> list = new LinkedList<NPC>();
    list.add(new Tifa());
    list.add(new Hinata());
    list.add(new MaryAnn());
    return list;
  }
}
