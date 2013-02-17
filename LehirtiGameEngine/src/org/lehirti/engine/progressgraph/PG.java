package org.lehirti.engine.progressgraph;

import org.lehirti.engine.res.TextAndImageKeyWithFlag;

public interface PG extends TextAndImageKeyWithFlag {
  public PG[] getParents();
  
  public ProgressGraph getProgressGraph();
}
