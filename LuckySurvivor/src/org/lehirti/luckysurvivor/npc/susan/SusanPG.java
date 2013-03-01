package org.lehirti.luckysurvivor.npc.susan;

import org.lehirti.engine.progressgraph.PG;
import org.lehirti.engine.progressgraph.ProgressGraph;
import org.lehirti.engine.state.StringState;

public enum SusanPG implements PG {
  UNKNOWN,
  MET(UNKNOWN);
  
  private static ProgressGraph PROGRESS_GRAPH = null;
  
  private final SusanPG[] parents;
  
  private SusanPG(final SusanPG... parents) {
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
    return new Susan().getName();
  }
}
