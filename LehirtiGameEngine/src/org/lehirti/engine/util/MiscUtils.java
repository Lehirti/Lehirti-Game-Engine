package org.lehirti.engine.util;

public final class MiscUtils {

  public static int max(final int[] widthPerLevel) {
    int max = Integer.MIN_VALUE;
    for (final int element : widthPerLevel) {
      if (element > max) {
        max = element;
      }
    }
    return max;
  }
  
}
