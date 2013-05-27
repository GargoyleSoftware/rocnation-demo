package co.gargoyle.rocnation.events;

import co.gargoyle.rocnation.service.MusicService;

public class MusicServiceConnectedEvent {

	public MusicService service;

	public MusicServiceConnectedEvent(MusicService service) {
		this.service = service;
	}

}
