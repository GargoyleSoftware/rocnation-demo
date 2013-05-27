package co.gargoyle.rocnation.events;

public class MusicTimeChangedEvent {

  /** Time in millis */
  public final long currentTime;
  /** Time in millis */
  public final long totalTime;

  public MusicTimeChangedEvent(long totalTime, long currentTime) {
    this.totalTime = totalTime;
    this.currentTime = currentTime;
  }

}
