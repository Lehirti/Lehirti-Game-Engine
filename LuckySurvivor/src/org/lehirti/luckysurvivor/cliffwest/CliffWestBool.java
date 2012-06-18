package org.lehirti.luckysurvivor.cliffwest;

import org.lehirti.engine.state.BoolState;

/**
 * state used in CliffWest; there are just flags (true/false);<br/>
 * the initial values can be set in CliffWestBool.properties
 */
public enum CliffWestBool implements BoolState {
  HAS_BEEN_HERE_BEFORE,
  HAS_MET_JORDAN;
}
