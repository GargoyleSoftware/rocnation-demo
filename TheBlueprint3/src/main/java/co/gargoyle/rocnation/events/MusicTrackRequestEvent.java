package co.gargoyle.rocnation.events;

import co.gargoyle.rocnation.model.Song;

public class MusicTrackRequestEvent {

	public final Song song;

	public MusicTrackRequestEvent(Song song) {
		this.song = song;
	}

}
