package co.gargoyle.rocnation.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "songs")
public class Song extends Model {

    // public static final String TITLE      = "title";
    // public static final String ARTIST     = "artist";
    // public static final String LYRIC_JSON = "lyric_json" ;
    // public static final String DURATION   = "duration" ;
    // public static final String TRACK      = "track" ;
    // public static final String AUDIO_URL  = "audio_url" ;
    // public static final String VIDEO_URL  = "video_url" ;


	public Song() {
		super();
	}

	@Column(name = "title")
	private String title;

	@Column(name = "artist")
	private String artist;

	@Column(name = "lyric_json")
	private String lyricJson;

	@Column(name = "duration")
	private int duration;

	@Column(name = "track")
	private int track;

	@Column(name = "audio_url")
	private String audioUrl;

	@Column(name = "video_url")
	private String videoUrl;


	public static List<Song> getAll() {
		return new Select()
			.from(Song.class)
			.execute();
	}

	@Override
	public String toString() {
		return title;
	}
}
