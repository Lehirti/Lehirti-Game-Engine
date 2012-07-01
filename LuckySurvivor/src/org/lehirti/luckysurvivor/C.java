package org.lehirti.luckysurvivor;

public enum C {
  MAIN(17);
  
  public final int requiredVersion;
  public boolean available = false;
  
  private C(final int requiredVersion) {
    this.requiredVersion = requiredVersion;
  }
}
