package org.lehirti.luckysurvivor.npc.tifa;

import org.lehirti.engine.progressgraph.PG;
import org.lehirti.engine.progressgraph.ProgressGraph;
import org.lehirti.engine.state.StringState;

public enum TifaPG implements PG {
  UNKNOWN,
  MET(UNKNOWN);
  
  private static ProgressGraph PROGRESS_GRAPH = null;
  
  private final TifaPG[] parents;
  
  private TifaPG(final TifaPG... parents) {
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
    return new Tifa().getName();
  }
}
