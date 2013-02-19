package org.lehirti.engine.progressgraph;

import org.lehirti.engine.res.TextAndImageKeyWithFlag;
import org.lehirti.engine.state.StringState;

public interface PG extends TextAndImageKeyWithFlag {
  public PG[] getParents();
  
  public ProgressGraph getProgressGraph();
  
  public StringState getName();
}
