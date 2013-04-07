package text;

import lge.res.text.OrderedTextKey;

public enum BreastSize implements OrderedTextKey {
  NON_EXISTANT(0),
  MINUSCULE(90),
  WEE(100),
  TINY(120),
  SMALL(130),
  PETITE(140),
  SYLPH_LIKE(150),
  AMPLE(160),
  VERY_AMPLE(170),
  CAPACIOUS(180),
  VERY_CAPACIOUS(190),
  QUITE_BIG(200),
  RATHER_BIG(210),
  BIG(220),
  NICELY_BIG(230),
  LARGE(240),
  PRETTY_LARGE(250),
  VERY_LARGE(260),
  REALLY_LARGE(270),
  QUITE_HUGE(280),
  HUGE(290),
  VERY_HUGE(300),
  REALLY_HUGE(310),
  IMMENSE(320),
  RATHER_VAST(330),
  VAST(340),
  REALLY_VAST(350),
  WALLOPING(360),
  BIG_WALLOPING(370),
  RATHER_MASSIVE(380),
  MASSIVE(390),
  ABSOLUTELY_MASSIVE(400), ;
  
  private final long startingSize;
  
  private BreastSize(final long startingSize) {
    this.startingSize = startingSize;
  }
  
  @Override
  public long getStartValue() {
    return this.startingSize;
  }
}
