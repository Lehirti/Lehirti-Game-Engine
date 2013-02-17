package org.lehirti.engine.progressgraph;

import org.lehirti.engine.state.State;

public enum TestGraph implements PG {
  UNKNOWN,
  KNOW_NAME(UNKNOWN),
  MET_HER(UNKNOWN),
  MET_HER_AND_KNOW_NAME(KNOW_NAME, MET_HER),
  HATED(MET_HER_AND_KNOW_NAME, MET_HER),
  LIKED(MET_HER_AND_KNOW_NAME),
  LOVED(LIKED);
  
  static {
    State.set(UNKNOWN, true);
    State.set(MET_HER, true);
  }
  
  private static ProgressGraph PROGRESS_GRAPH = null;
  
  private final TestGraph[] parents;
  
  private TestGraph(final TestGraph... parents) {
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
}
