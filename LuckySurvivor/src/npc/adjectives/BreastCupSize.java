package npc.adjectives;

import lge.res.text.OrderedTextKey;

public enum BreastCupSize implements OrderedTextKey {
  NONE(0),
  AAA(90),
  AA(100),
  A(120),
  BIG_A(130),
  B(140),
  BIG_B(150),
  C(160),
  BIG_C(170),
  D(180),
  BIG_D(190),
  E(200),
  BIG_E(210),
  F(220),
  BIG_F(230),
  G(240),
  BIG_G(250),
  H(260),
  BIG_H(270),
  I(280),
  BIG_I(90),
  J(300),
  BIG_J(310),
  K(320),
  BIG_K(330),
  L(340),
  BIG_L(350),
  M(360),
  BIG_M(370),
  N(380),
  BIG_N(390),
  BIGGEST_POSSIBLE_CUP(400), ;
  
  private final long startingSize;
  
  private BreastCupSize(final long startingSize) {
    this.startingSize = startingSize;
  }
  
  @Override
  public long getStartValue() {
    return this.startingSize;
  }
}
