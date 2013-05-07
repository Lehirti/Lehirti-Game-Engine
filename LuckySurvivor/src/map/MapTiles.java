package map;

import lge.res.images.ImageKey;

public enum MapTiles implements ImageKey {
  TILE_0_0,
  TILE_0_1,
  TILE_0_2,
  TILE_0_3,
  TILE_0_4,
  TILE_0_5,
  TILE_0_6,
  TILE_1_0,
  TILE_1_1,
  TILE_1_2,
  TILE_1_3,
  TILE_1_4,
  TILE_1_5,
  TILE_1_6,
  TILE_2_0,
  TILE_2_1,
  TILE_2_2,
  TILE_2_3,
  TILE_2_4,
  TILE_2_5,
  TILE_2_6,
  TILE_3_0,
  TILE_3_1,
  TILE_3_2,
  TILE_3_3,
  TILE_3_4,
  TILE_3_5,
  TILE_3_6,
  TILE_4_0,
  TILE_4_1,
  TILE_4_2,
  TILE_4_3,
  TILE_4_4,
  TILE_4_5,
  TILE_4_6,
  TILE_5_0,
  TILE_5_1,
  TILE_5_2,
  TILE_5_3,
  TILE_5_4,
  TILE_5_5,
  TILE_5_6,
  TILE_6_0,
  TILE_6_1,
  TILE_6_2,
  TILE_6_3,
  TILE_6_4,
  TILE_6_5,
  TILE_6_6, ;
  
  public static MapTiles get(final long x, final long y) {
    String xString;
    if (x < 0) {
      xString = "N" + (-x);
    } else {
      xString = String.valueOf(x);
    }
    String yString;
    if (y < 0) {
      yString = "N" + (-y);
    } else {
      yString = String.valueOf(y);
    }
    return valueOf("TILE_" + xString + "_" + yString);
  }
}
