package sss;

import static sss.SexToyCategory.*;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import lge.res.TextAndImageKeyWithFlag;
import lge.state.State;


public enum SexToy implements TextAndImageKeyWithFlag {
  TINY_DILDO(PUSSY, ASS),
  SMALL_DILDO(PUSSY, ASS),
  DILDO(PUSSY, ASS),
  BIG_DILDO(PUSSY, ASS),
  LARGE_DILDO(PUSSY, ASS);
  
  private final Set<SexToyCategory> categories = EnumSet.noneOf(SexToyCategory.class);
  
  private SexToy(final SexToyCategory... categories) {
    for (final SexToyCategory cat : categories) {
      this.categories.add(cat);
    }
  }
  
  public boolean belongsToCategory(final SexToyCategory category) {
    return this.categories.contains(category);
  }
  
  public static List<SexToy> getAllAvailable(final SexToyCategory category) {
    final List<SexToy> availableToys = new LinkedList<>();
    for (final SexToy toy : values()) {
      if (toy.belongsToCategory(category) && State.is(toy)) {
        availableToys.add(toy);
      }
    }
    return availableToys;
  }
}
