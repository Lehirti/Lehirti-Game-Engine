package org.lehirti.engine.progressgraph;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Vector;

import org.lehirti.engine.events.AltScreenToGameEvent;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.events.EventNode;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.progressgraph.ProgressGraph.ProgressCommon;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.state.State;
import org.lehirti.engine.util.ClassFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProgressEvent extends EventNode<NullState> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProgressEvent.class);
  static {
    /*
     * make sure, all classes that provide progress graphs are loaded and registered with AllPGs, before instances of
     * this class are created
     */
    final Vector<Class<?>> progressGraph = new ClassFinder().findSubclasses(PG.class.getName());
    for (final Class<?> pg : progressGraph) {
      AllPGs.add((Class<PG>) pg);
      LOGGER.debug("Loaded progressGraph: {}", pg.getName());
    }
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
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK, new AltScreenToGameEvent());
  }
}
