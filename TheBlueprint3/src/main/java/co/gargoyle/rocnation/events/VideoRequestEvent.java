package co.gargoyle.rocnation.events;

import co.gargoyle.rocnation.model.Video;

public class VideoRequestEvent {

	public final Video video;
	public final int index;

	public VideoRequestEvent(Video video, int index) {
		this.video = video;
		this.index = index;
	}

}
