package org.lehirti.engine.progressgraph;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.lehirti.engine.progressgraph.ProgressGraph.ProgressCommon;

public class AllPGs {
  private static final Collection<Class<PG>> ALL_PGS = new LinkedHashSet<Class<PG>>();
  
  private AllPGs() {
  }
  
  public static void add(final Class<PG> pg) {
    ALL_PGS.add(pg);
  }
  
  public static Collection<Class<PG>> get() {
    return Collections.unmodifiableCollection(ALL_PGS);
  }
  
  public static List<ProgressGraph> get(final ProgressCommon generalProgress) {
    final List<ProgressGraph> retList = new LinkedList<ProgressGraph>();
    for (final Class<PG> clazz : ALL_PGS) {
      final ProgressGraph graph = clazz.getEnumConstants()[0].getProgressGraph();
      if (graph.getGeneralProgress() == generalProgress) {
        retList.add(graph);
      }
    }
    return retList;
  }
}
