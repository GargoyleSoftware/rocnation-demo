package co.gargoyle.rocnation.events;

public class MusicTimeChangedEvent {

    public final double playbackTime;

    public MusicTimeChangedEvent(double playbackTime) {
        this.playbackTime = playbackTime;
    }
}