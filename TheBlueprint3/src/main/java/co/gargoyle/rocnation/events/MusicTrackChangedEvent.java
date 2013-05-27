package co.gargoyle.rocnation.events;

import co.gargoyle.rocnation.model.Song;

public class MusicTrackChangedEvent {

	public final Song song;
	public final long totalTime;

	public MusicTrackChangedEvent(Song song, long totalTime) {
		this.song = song;
		this.totalTime = totalTime;
	}

}
