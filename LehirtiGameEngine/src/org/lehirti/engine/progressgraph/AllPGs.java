package org.lehirti.engine.progressgraph;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

public class AllPGs {
  private static final Collection<Class<PG>> ALL_PGS = new LinkedHashSet<Class<PG>>();
  
  private AllPGs() {
  }
  
  public static void add(final Class<PG> pg) {
    ALL_PGS.add(pg);
  }
  
  public static Collection<Class<PG>> get() {
    return Collections.unmodifiableCollection(ALL_PGS);
  }
}
