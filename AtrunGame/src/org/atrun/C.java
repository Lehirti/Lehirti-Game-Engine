package org.atrun;

public enum C {
  MAIN(2),
  DUCKGIRLS(1);
  
  public final int requiredVersion;
  public boolean available = false;
  
  private C(final int requiredVersion) {
    this.requiredVersion = requiredVersion;
  }
}
