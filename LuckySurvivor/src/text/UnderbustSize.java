package text;

import lge.res.text.OrderedTextKey;

public enum UnderbustSize implements OrderedTextKey {
  TINY(0),
  PETITE(600),
  SLIM(650),
  SLENDER(700),
  SVELTE(750),
  CURVY(800),
  BUXOM(850),
  MEATY(900), ;
  
  private final long startingSize;
  
  private UnderbustSize(final long startingSize) {
    this.startingSize = startingSize;
  }
  
  @Override
  public long getStartValue() {
    return this.startingSize;
  }
}
