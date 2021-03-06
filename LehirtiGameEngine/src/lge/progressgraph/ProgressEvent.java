package lge.progressgraph;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

import lge.events.Event.NullState;
import lge.events.EventNode;
import lge.gui.Key;
import lge.progressgraph.ProgressGraph.ProgressCommon;
import lge.res.ResourceCache;
import lge.res.images.ImgChange;
import lge.res.text.CommonText;
import lge.res.text.TextWrapper;
import lge.state.State;
import lge.util.ClassFinder;
import lge.util.ClassFinder.ClassWorker;
import lge.util.ClassFinder.SuperClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProgressEvent extends EventNode<NullState> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProgressEvent.class);
  static {
    /*
     * make sure, all classes that provide progress graphs are loaded and registered with AllPGs, before instances of
     * this class are created
     */
    ClassFinder.workWithClasses(new ClassWorker() {
      
      @Override
      public void doWork(final List<Class<?>> progressGraph) {
        for (final Class<?> pg : progressGraph) {
          AllPGs.add((Class<PG>) pg);
          LOGGER.debug("Loaded progressGraph: {}", pg.getName());
        }
      }
      
      @Override
      public SuperClass getSuperClass() {
        return SuperClass.PG_SUPERCLASS;
      }
      
      @Override
      public String getDescription() {
        return "[Progress Graph Loader]";
      }
    });
  }
  
  private ProgressGraph pg;
  
  // for loading/saving
  public ProgressEvent() {
    this((ProgressGraph) null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.pg = new ProgressGraph((Class<? extends PG>) in.readObject());
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.pg.getPG());
  }
  
  public ProgressEvent(final Class<? extends PG> selectedItem) {
    this.pg = new ProgressGraph(selectedItem);
  }
  
  public ProgressEvent(final ProgressGraph selectedItem) {
    this.pg = selectedItem;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    if (this.pg != null) {
      return ImgChange.setFG(this.pg.getActiveNode().getId());
    }
    return ImgChange.clearFG();
  }
  
  @Override
  protected void doEvent() {
    final ProgressCommon progress = this.pg.getGeneralProgress();
    setText(progress);
    final List<ProgressGraph> sames = AllPGs.get(progress);
    
    List<ProgressGraph> prevs = null;
    ProgressCommon prevProg = progress.getPrevious();
    do {
      prevs = AllPGs.get(prevProg);
      prevProg = prevProg.getPrevious();
    } while (prevs.isEmpty() && prevProg != progress);
    prevProg = prevProg.getNext();
    
    List<ProgressGraph> nexts = null;
    ProgressCommon nextProg = progress.getNext();
    do {
      nexts = AllPGs.get(nextProg);
      nextProg = nextProg.getNext();
    } while (nexts.isEmpty() && nextProg != progress);
    nextProg = nextProg.getPrevious();
    
    for (int i = 0; i < sames.size(); i++) {
      final ProgressGraph graph = sames.get(i);
      if (graph.equals(this.pg)) {
        if (i != 0) {
          addOption(Key.OPTION_NORTH, CommonText.OPTION_PREVIOUS, new ProgressEvent(sames.get(i - 1)));
        }
        if (i + 1 < sames.size()) {
          addOption(Key.OPTION_SOUTH, CommonText.OPTION_NEXT, new ProgressEvent(sames.get(i + 1)));
        }
        addText(ResourceCache.get(CommonText.MARKER));
      }
      
      TextWrapper nameWrapper;
      if (progress == ProgressCommon.UNKNOWN) {
        nameWrapper = ResourceCache.get(CommonText.UNKNOWN_NAME);
      } else {
        nameWrapper = ResourceCache.get(CommonText.PARAMETER);
        nameWrapper.addParameter(State.get(this.pg.getName()));
      }
      addText(nameWrapper);
      addText(CommonText.NEWLINE);
    }
    if (!prevs.isEmpty()) {
      addOption(Key.OPTION_WEST, prevProg, new ProgressEvent(prevs.get(0)));
    }
    if (!nexts.isEmpty()) {
      addOption(Key.OPTION_EAST, nextProg, new ProgressEvent(nexts.get(0)));
    }
  }
}
