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
    final int seconds = DDhhmmss % 100;
    final int DDhhmm = DDhhmmss / 100;
    int minutes = DDhhmm % 100;
    final int DDhh = DDhhmm / 100;
    int hours = DDhh % 100;
    int days = DDhh / 100;
    
    long newSeconds = StateObject.get(SECOND) + seconds;
    while (newSeconds >= 60) {
      newSeconds -= 60;
      minutes++;
    }
    StateObject.set(SECOND, newSeconds);
    
    long newMinutes = StateObject.get(MINUTE) + minutes;
    while (newMinutes >= 60) {
      newMinutes -= 60;
      hours++;
    }
    StateObject.set(MINUTE, newMinutes);
    
    long newHours = StateObject.get(HOUR) + hours;
    while (newHours >= 24) {
      newHours -= 24;
      days++;
    }
    StateObject.set(HOUR, newHours);
    
    StateObject.set(DAY, StateObject.get(DAY) + days);
    
    updateScreen();
  }
  
  /**
   * advance time to next hhmmss, possible advancing the day; will do nothing if current hhmmss equals parameter hhmmss
   * 
   * @param hhmmss
   */
  public static void advanceTo(final int hhmmss) {
    final long currenthhmmss = (StateObject.get(HOUR) * 10000) + (StateObject.get(MINUTE) * 100)
        + StateObject.get(SECOND);
    if (hhmmss < currenthhmmss) {
      StateObject.set(DAY, StateObject.get(DAY) + 1);
    }
    
    final int seconds = hhmmss % 100;
    final int hhmm = hhmmss / 100;
    final int minutes = hhmm % 100;
    final int hours = hhmm / 100;
    StateObject.set(HOUR, hours);
    StateObject.set(MINUTE, minutes);
    StateObject.set(SECOND, seconds);
    updateScreen();
  }
  
  /**
   * advance time to next week day toDay and time hhmmss; will do nothing if current datetime equals parameter datetime
   * 
   * @param toDay
   * @param hhmmss
   */
  public static void advanceTo(final DayOfWeek toDay, final int hhmmss) {
    advanceTo(hhmmss);
    while (toDay != getCurrentDayOfWeek()) {
      StateObject.set(DAY, StateObject.get(DAY) + 1);
    }
    updateScreen();
  }
  
  private static void updateScreen() {
    final TextWrapper day = ResourceCache.get(CommonText.DAY);
    day.addParameter(String.valueOf(StateObject.get(DAY)));
    Main.STATS_AREA.setText(day);
    Main.STATS_AREA.addText(ResourceCache.get(getCurrentDayOfWeek()));
    Main.STATS_AREA.addText(ResourceCache.get(CommonText.BLANK));
    final TextWrapper time = ResourceCache.get(CommonText.TIME_FORMAT);
    time.addParameter(padTo2Digits(StateObject.get(HOUR)));
    time.addParameter(padTo2Digits(StateObject.get(MINUTE)));
    time.addParameter(padTo2Digits(StateObject.get(SECOND)));
    Main.STATS_AREA.addText(time);
  }
  
  private static String padTo2Digits(final long l) {
    return l < 10 ? "0" + l : String.valueOf(l);
  }
}
