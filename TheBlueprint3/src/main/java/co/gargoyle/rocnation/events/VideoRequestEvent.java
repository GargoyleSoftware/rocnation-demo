package co.gargoyle.rocnation.events;

import co.gargoyle.rocnation.model.Video;

public class VideoRequestEvent {

	public final Video video;

	public VideoRequestEvent(Video video) {
		this.video = video;
	}

}
