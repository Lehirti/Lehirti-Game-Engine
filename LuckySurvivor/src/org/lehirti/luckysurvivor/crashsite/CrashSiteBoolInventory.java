package org.lehirti.luckysurvivor.crashsite;

import org.lehirti.engine.state.BoolState;
import org.lehirti.engine.state.Inventory;

public enum CrashSiteBoolInventory implements BoolState, Inventory {
  WATCH,
  SHOVEL,
  METAL_STRUT,
  LONG_METAL_POLES,
  SHEETS_OF_FABRIC,
  STRETCHER,
  PEANUTS_AND_BOTTLED_WATER;
}
