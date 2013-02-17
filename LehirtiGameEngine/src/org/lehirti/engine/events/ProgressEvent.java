package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Vector;

import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.progressgraph.AllPGs;
import org.lehirti.engine.progressgraph.PG;
import org.lehirti.engine.progressgraph.ProgressGraph;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.util.ClassFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProgressEvent extends EventNode<NullState> implements Externalizable {
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
    this(null);
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
  
  @Override
  protected ImgChange updateImageArea() {
    if (this.pg != null) {
      return ImgChange.setFG(this.pg.getActiveNode().getId());
    }
    return ImgChange.clearFG();
  }
  
  @Override
  protected void doEvent() {
    // TODO
    // setText(CommonText.PROGRESS);
    // if (this.selectedItem != null) {
    // InventoryMap.setSelectedItem(this.selectedItem);
    //
    // final SortedMap<String, LinkedHashMap<Inventory, Object>> sortedInventory = InventoryMap.getSortedInventory();
    //
    // final List<Map.Entry<Inventory, Object>> items = new LinkedList<Map.Entry<Inventory, Object>>();
    // int indexOfSelectedItem = 0;
    //
    // for (final LinkedHashMap<Inventory, Object> map : sortedInventory.values()) {
    // for (final Map.Entry<Inventory, Object> entry : map.entrySet()) {
    // items.add(entry);
    // if (entry.getKey().equals(this.selectedItem)) {
    // indexOfSelectedItem = items.size() - 1;
    // }
    // }
    // }
    // int startIndex = indexOfSelectedItem - 5;
    // if (startIndex < 0) {
    // startIndex = 0;
    // }
    // int endIndex = indexOfSelectedItem + 5;
    // if (endIndex >= items.size()) {
    // endIndex = items.size() - 1;
    // }
    // for (int i = startIndex; i <= endIndex; i++) {
    // final Map.Entry<Inventory, Object> entry = items.get(i);
    // if (entry.getKey().equals(this.selectedItem)) {
    // addText(ResourceCache.get(CommonText.MARKER));
    // }
    // final TextWrapper tw = ResourceCache.get((TextKey) entry.getKey());
    // tw.addParameter(entry.getValue().toString());
    // addText(tw);
    // addText(CommonText.NEWLINE);
    // }
    // if (indexOfSelectedItem != startIndex) {
    // addOption(Key.OPTION_NORTH, CommonText.OPTION_PREVIOUS, new ProgressEvent(items.get(indexOfSelectedItem - 1)
    // .getKey()));
    // }
    // if (indexOfSelectedItem != endIndex) {
    // addOption(Key.OPTION_SOUTH, CommonText.OPTION_NEXT, new ProgressEvent(items.get(indexOfSelectedItem + 1)
    // .getKey()));
    // }
    // }
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK, new InventoryToGameEvent());
  }
}
