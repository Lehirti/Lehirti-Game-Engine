package org.lehirti.luckysurvivor.crashsite;

import java.util.LinkedList;
import java.util.List;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.state.State;
import org.lehirti.luckysurvivor.npc.NPC;
import org.lehirti.luckysurvivor.npc.hinata.Hinata;
import org.lehirti.luckysurvivor.npc.tifa.Tifa;
import org.lehirti.luckysurvivor.npc.maryann.MaryAnn;
import org.lehirti.luckysurvivor.npc.susan.Susan;
import org.lehirti.luckysurvivor.peninsulabasinjungle.SusanVineRape;

public class CrashSiteNPCs {
  public static List<NPC> getNPCs() {
    final List<NPC> list = new LinkedList<NPC>();
    list.add(new Tifa());
    list.add(new Hinata());
    list.add(new MaryAnn());
    if(State.getEventCount(SusanVineRape.class) > 0) {
        list.add(new Susan());
    }
    return list;
  }
}

