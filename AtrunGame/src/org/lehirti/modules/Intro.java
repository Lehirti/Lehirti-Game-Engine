package org.lehirti.modules;

import org.atrun.modules.intro.Intro01;
import org.lehirti.Main;

public class Intro implements ModuleInitializer {
  static {
    Main.nextEvent = new Intro01();
  }
}
