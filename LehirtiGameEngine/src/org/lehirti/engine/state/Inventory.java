package org.lehirti.engine.state;

import org.lehirti.engine.res.text.TextKey;

/**
 * classes that implement this interface describe state are treated as inventory, meaning they will show up in the
 * inventory screen
 */
public interface Inventory extends State, TextKey {
  
}
