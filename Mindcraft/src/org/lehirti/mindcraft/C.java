package org.lehirti.mindcraft;

public enum C {
  MAIN(3),
  DUCKGIRLS(2);
  
  public final int requiredVersion;
  public boolean available = false;
  
  private C(final int requiredVersion) {
    this.requiredVersion = requiredVersion;
  }
}
