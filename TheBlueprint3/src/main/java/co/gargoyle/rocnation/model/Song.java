package co.gargoyle.rocnation.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "songs")
public class Song extends Model {

	public Song() {
		super();
	}

	@Column(name = "title")
	public String title;

	@Column(name = "artist")
	public String artist;

	@Column(name = "lyric_json")
	public String lyricJson;

	@Column(name = "duration")
	public int duration;

	@Column(name = "track")
	public int track;

	@Column(name = "audio_url")
	public String audioUrl;

	@Column(name = "video_url")
	public String videoUrl;


	public static List<Song> getAll() {
		return new Select()
			.from(Song.class)
			.execute();
	}

	public static Song getByTrack(int trackNumber) {
		return new Select()
		.from(Song.class)
		.where("track = ?", trackNumber)
		.executeSingle();
	}

	@Override
	public String toString() {
		return title;
	}

	public String fullName() {
		if (artist != null && ! artist.equals("")) {
			return String.format("%s - %s", title, artist);
		} else {
			return title;
		}
	}

}
