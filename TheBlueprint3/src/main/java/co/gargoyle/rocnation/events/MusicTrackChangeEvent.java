package co.gargoyle.rocnation.events;

import co.gargoyle.rocnation.model.Song;

public class MusicTrackChangeEvent {

	public final Song song;

	public MusicTrackChangeEvent(Song song) {
		this.song = song;
	}

}
