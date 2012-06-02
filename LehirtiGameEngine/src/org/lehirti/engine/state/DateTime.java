package org.lehirti.engine.state;

import org.lehirti.engine.Main;
import org.lehirti.engine.res.ResourceCache;
import org.lehirti.engine.res.text.CommonText;
import org.lehirti.engine.res.text.TextKey;
import org.lehirti.engine.res.text.TextWrapper;

public enum DateTime implements IntState {
  FIRST_DAY,
  
  DAY,
  
  HOUR,
  MINUTE,
  SECOND;
  
  public enum DayOfWeek implements TextKey {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;
  }
  
  @Override
  public Long defaultValue() {
    return Long.valueOf(0L);
  }
  
  public static void setFirstDayOfWeek(final DayOfWeek firstDay) {
    StateObject.set(FIRST_DAY, firstDay.ordinal());
  }
  
  public static DayOfWeek getFirstDayOfWeek() {
    return DayOfWeek.values()[(int) StateObject.get(FIRST_DAY)];
  }
  
  public static DayOfWeek getCurrentDayOfWeek() {
    int currentDayOfWeek = (int) StateObject.get(FIRST_DAY);
    currentDayOfWeek += (int) StateObject.get(DAY);
    final DayOfWeek[] daysOfWeek = DayOfWeek.values();
    currentDayOfWeek %= daysOfWeek.length;
    return daysOfWeek[currentDayOfWeek];
  }
  
  public static void init(final DayOfWeek firstDay, final int day, final int hour, final int minute, final int second) {
    setFirstDayOfWeek(firstDay);
    StateObject.set(DAY, day);
    StateObject.set(HOUR, hour);
    StateObject.set(MINUTE, minute);
    StateObject.set(SECOND, second);
    updateScreen();
  }
  
  public static void advanceBy(final int DDhhmmss) {
    // TODO
    updateScreen();
  }
  
  public static void advanceTo(final int hhmmss) {
    // TODO
    updateScreen();
  }
  
  public static void advanceTo(final DayOfWeek toDay, final int hhmmss) {
    // TODO
    updateScreen();
  }
  
  private static void updateScreen() {
    final TextWrapper day = ResourceCache.get(CommonText.DAY);
    day.addParameter(String.valueOf(StateObject.get(DAY)));
    Main.STATS_AREA.setText(day);
    Main.STATS_AREA.addText(ResourceCache.get(getCurrentDayOfWeek()));
    Main.STATS_AREA.addText(ResourceCache.get(CommonText.BLANK));
    final TextWrapper time = ResourceCache.get(CommonText.TIME_FORMAT);
    time.addParameter(String.valueOf(StateObject.get(HOUR)));
    time.addParameter(String.valueOf(StateObject.get(MINUTE)));
    time.addParameter(String.valueOf(StateObject.get(SECOND)));
    Main.STATS_AREA.addText(time);
  }
}
