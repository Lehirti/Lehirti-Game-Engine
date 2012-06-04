package org.lehirti.engine.sex;

import static org.lehirti.engine.sex.SexFeature.*;

public enum Sex {
  MALE(COCK, ANY),
  FEMALE(PUSSY, BREASTS, ANY),
  FUTA(COCK, PUSSY, BREASTS, ANY);
  
  private final SexFeature[] features;
  
  private Sex(final SexFeature... features) {
    this.features = features;
  }
  
  public boolean has(final SexFeature feature) {
    for (final SexFeature feature2 : this.features) {
      if (feature2 == feature) {
        return true;
      }
    }
    return false;
  }
}
