package co.gargoyle.rocnation.events;


public class MusicTimeRequestEvent {

	public final long requestedTime;

	public MusicTimeRequestEvent(long requestedTime) {
		this.requestedTime = requestedTime;
	}

}
