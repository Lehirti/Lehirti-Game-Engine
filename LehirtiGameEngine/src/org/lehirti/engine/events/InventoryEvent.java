package org.lehirti.engine.events;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;

import org.lehirti.engine.Main;
import org.lehirti.engine.events.Event.NullState;
import org.lehirti.engine.gui.Key;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.images.ImgChange;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;
import org.lehirti.engine.state.Inventory;
import org.lehirti.engine.state.InventoryMap;

public final class InventoryEvent extends EventNode<NullState> implements Externalizable {
  private Inventory selectedItem;
  
  // for loading/saving
  public InventoryEvent() {
    this(null);
  }
  
  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    this.selectedItem = (Inventory) in.readObject();
  }
  
  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(this.selectedItem);
  }
  
  public InventoryEvent(final Inventory selectedItem) {
    this.selectedItem = selectedItem;
  }
  
  @Override
  protected ImgChange updateImageArea() {
    if (this.selectedItem != null) {
      return ImgChange.setFG(this.selectedItem);
    }
    return ImgChange.clearFG();
  }
  
  @Override
  protected void doEvent() {
    setText(CommonText.INVENTORY);
    if (this.selectedItem != null) {
      InventoryMap.setSelectedItem(this.selectedItem);
      
      final SortedMap<String, LinkedHashMap<Inventory, Object>> sortedInventory = InventoryMap.getSortedInventory();
      
      for (final LinkedHashMap<Inventory, Object> map : sortedInventory.values()) {
        for (final Map.Entry<Inventory, Object> entry : map.entrySet()) {
          if (entry.getKey().equals(this.selectedItem)) {
            Main.INVENTORY_TEXT_AREA.addText(ResourceCache.get(CommonText.MARKER));
            Main.INVENTORY_IMAGE_AREA.setImage(entry.getKey());
          }
          final TextWrapper tw = ResourceCache.get((TextKey) entry.getKey());
          tw.addParameter(entry.getValue().toString());
          Main.INVENTORY_TEXT_AREA.addText(tw);
        }
      }
    }
    addOption(Key.OPTION_LEAVE, CommonText.OPTION_BACK, new InventoryToGameEvent());
  }
}
