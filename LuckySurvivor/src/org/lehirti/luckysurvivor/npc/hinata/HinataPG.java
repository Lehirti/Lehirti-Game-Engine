package org.lehirti.luckysurvivor.npc.hinata;

import org.lehirti.engine.progressgraph.PG;
import org.lehirti.engine.progressgraph.ProgressGraph;
import org.lehirti.engine.state.StringState;

public enum HinataPG implements PG {
  UNKNOWN,
  MET(UNKNOWN);
  
  private static ProgressGraph PROGRESS_GRAPH = null;
  
  private final HinataPG[] parents;
  
  private HinataPG(final HinataPG... parents) {
    this.parents = parents;
  }
  
  @Override
  public PG[] getParents() {
    return this.parents;
  }
  
  @Override
  public ProgressGraph getProgressGraph() {
    if (PROGRESS_GRAPH == null) {
      PROGRESS_GRAPH = new ProgressGraph(this.getClass());
    }
    return PROGRESS_GRAPH;
  }
  
  @Override
  public StringState getName() {
    return new Hinata().getName();
  }
}
