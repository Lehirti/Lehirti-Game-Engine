package lge.state;

import lge.state.DateTime.DayOfWeek;

public class TimeInterval {
  private final boolean noChange;
  private final boolean relative;
  private final int DDhhmmss;
  private final DayOfWeek dayOfWeek;
  
  private TimeInterval() {
    this.noChange = true;
    this.relative = false;
    this.DDhhmmss = 0;
    this.dayOfWeek = null;
  }
  
  private TimeInterval(final boolean relative, final int DDhhmmss, final DayOfWeek dayOfWeek) {
    this.noChange = false;
    this.relative = relative;
    this.DDhhmmss = DDhhmmss;
    this.dayOfWeek = dayOfWeek;
  }
  
  public static TimeInterval noAdvance() {
    return new TimeInterval();
  }
  
  public static TimeInterval advanceTo(final int hhmmss) {
    return new TimeInterval(false, hhmmss, null);
  }
  
  public static TimeInterval advanceTo(final DayOfWeek dayOfWeek, final int hhmmss) {
    return new TimeInterval(false, hhmmss, dayOfWeek);
  }
  
  public static TimeInterval advanceBy(final int DDhhmmss) {
    return new TimeInterval(true, DDhhmmss, null);
  }
  
  public void advance() {
    if (this.noChange) {
      return;
    }
    if (this.relative) {
      DateTime.advanceBy(this.DDhhmmss);
    } else {
      if (this.dayOfWeek != null) {
        DateTime.advanceTo(this.dayOfWeek, this.DDhhmmss);
      } else {
        DateTime.advanceTo(this.DDhhmmss);
      }
    }
  }
}
