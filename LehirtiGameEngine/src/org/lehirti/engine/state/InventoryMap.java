package org.lehirti.engine.state;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;

import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.text.TextKey;

public class InventoryMap<K extends AbstractState, V> extends LinkedHashMap<K, V> {
  private static final long serialVersionUID = 1L;
  
  private static final Map<Inventory, Object> INVENTORY = new HashMap<>();
  
  private static Stack<Inventory> LAST_SELECTED_ITEMS = new Stack<>();
  
  @Override
  public V put(final K key, final V value) {
    if (key instanceof Inventory) {
      final Inventory invKey = (Inventory) key;
      if (invKey instanceof BoolState) {
        final Boolean boolValue = (Boolean) value;
        if (boolValue.booleanValue()) {
          INVENTORY.put(invKey, boolValue);
          LAST_SELECTED_ITEMS.push(invKey);
        } else {
          INVENTORY.remove(invKey);
          LAST_SELECTED_ITEMS.remove(invKey);
        }
      } else if (invKey instanceof IntState) {
        final Long longValue = (Long) value;
        if (longValue.longValue() == 0L) {
          INVENTORY.remove(invKey);
          LAST_SELECTED_ITEMS.remove(invKey);
        } else {
          INVENTORY.put(invKey, longValue);
          LAST_SELECTED_ITEMS.push(invKey);
        }
      } else if (invKey instanceof StringState) {
        final String stringValue = (String) value;
        if (stringValue.equals("")) {
          INVENTORY.remove(invKey);
          LAST_SELECTED_ITEMS.remove(invKey);
        } else {
          INVENTORY.put(invKey, stringValue);
          LAST_SELECTED_ITEMS.push(invKey);
        }
      } else {
        // TODO
      }
    }
    return super.put(key, value);
  }
  
  @Override
  public void clear() {
    INVENTORY.clear();
    super.clear();
  }
  
  public static Inventory getSelectedItem() {
    if (LAST_SELECTED_ITEMS.isEmpty()) {
      return null;
    }
    return LAST_SELECTED_ITEMS.peek();
  }
  
  public static void setSelectedItem(final Inventory item) {
    LAST_SELECTED_ITEMS.remove(item);
    LAST_SELECTED_ITEMS.push(item);
  }
  
  public static SortedMap<String, LinkedHashMap<Inventory, Object>> getSortedInventory() {
    final SortedMap<String, LinkedHashMap<Inventory, Object>> sortedInventory = new TreeMap<>();
    for (final Map.Entry<Inventory, Object> invEntry : INVENTORY.entrySet()) {
      final String rawValue = ResourceCache.getNullable((TextKey) invEntry.getKey()).getRawValue();
      LinkedHashMap<Inventory, Object> inventoryPerName = sortedInventory.get(rawValue);
      if (inventoryPerName == null) {
        inventoryPerName = new LinkedHashMap<>();
        sortedInventory.put(rawValue, inventoryPerName);
      }
      inventoryPerName.put(invEntry.getKey(), invEntry.getValue());
    }
    return sortedInventory;
  }
}
