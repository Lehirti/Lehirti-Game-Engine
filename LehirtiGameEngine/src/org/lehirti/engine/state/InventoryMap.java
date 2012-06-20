package org.lehirti.engine.state;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lehirti.engine.Main;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;

public class InventoryMap<K extends AbstractState, V> extends LinkedHashMap<K, V> {
  private static final long serialVersionUID = 1L;
  
  private static final Map<Inventory, Object> INVENTORY = new HashMap<Inventory, Object>();
  
  @Override
  public V put(final K key, final V value) {
    if (key instanceof Inventory) {
      final Inventory invKey = (Inventory) key;
      if (invKey instanceof BoolState) {
        final Boolean boolValue = (Boolean) value;
        if (boolValue.booleanValue()) {
          INVENTORY.put(invKey, boolValue);
        } else {
          INVENTORY.remove(invKey);
        }
      } else if (invKey instanceof IntState) {
        final Long longValue = (Long) value;
        if (longValue.longValue() == 0L) {
          INVENTORY.remove(invKey);
        } else {
          INVENTORY.put(invKey, longValue);
        }
      } else if (invKey instanceof StringState) {
        final String stringValue = (String) value;
        if (stringValue.equals("")) {
          INVENTORY.remove(invKey);
        } else {
          INVENTORY.put(invKey, stringValue);
        }
      } else {
        // TODO
      }
      updateInventoryScreen();
    }
    return super.put(key, value);
  }
  
  @Override
  public void clear() {
    INVENTORY.clear();
    updateInventoryScreen();
    super.clear();
  }
  
  private void updateInventoryScreen() {
    final SortedMap<String, LinkedHashMap<Inventory, Object>> sortedInventory = new TreeMap<String, LinkedHashMap<Inventory, Object>>();
    for (final Map.Entry<Inventory, Object> invEntry : INVENTORY.entrySet()) {
      final String rawValue = ResourceCache.get((TextKey) invEntry.getKey()).getRawValue();
      LinkedHashMap<Inventory, Object> inventoryPerName = sortedInventory.get(rawValue);
      if (inventoryPerName == null) {
        inventoryPerName = new LinkedHashMap<Inventory, Object>();
        sortedInventory.put(rawValue, inventoryPerName);
      }
      inventoryPerName.put(invEntry.getKey(), invEntry.getValue());
    }
    Main.INVENTORY_AREA.setText(ResourceCache.get(CommonText.INVENTORY));
    for (final LinkedHashMap<Inventory, Object> map : sortedInventory.values()) {
      for (final Map.Entry<Inventory, Object> entry : map.entrySet()) {
        final TextWrapper tw = ResourceCache.get((TextKey) entry.getKey());
        tw.addParameter(entry.getValue().toString());
        Main.INVENTORY_AREA.addText(tw);
      }
    }
  }
}
