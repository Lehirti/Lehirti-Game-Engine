package crashsite;

import java.util.LinkedList;
import java.util.List;

import lge.state.State;

import npc.NPC;
import npc.hinata.Hinata;
import npc.maryann.MaryAnn;
import npc.susan.Susan;
import npc.tifa.Tifa;

import peninsulabasinjungle.SusanVineRape;

public class CrashSiteNPCs {
  public static List<NPC> getNPCs() {
    final List<NPC> list = new LinkedList<>();
    list.add(new Tifa());
    list.add(new Hinata());
    list.add(new MaryAnn());
    if (State.getEventCount(SusanVineRape.class) > 0) {
      list.add(new Susan());
    }
    return list;
  }
}
