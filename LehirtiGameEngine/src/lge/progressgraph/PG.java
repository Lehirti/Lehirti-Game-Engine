package lge.progressgraph;

import lge.res.TextAndImageKeyWithFlag;
import lge.state.StringState;

public interface PG extends TextAndImageKeyWithFlag {
  public PG[] getParents();
  
  public ProgressGraph getProgressGraph();
  
  public StringState getName();
}
