package co.gargoyle.rocnation.model;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "videos")
public class Video extends Model {

	public static final int MUSIC_VIDEO       = 1;
	public static final int INTERVIEW         = 2;
	public static final int BEHIND_THE_SCENES = 3;

	@Column(name = "title")
		public String title;

	@Column(name = "type")
		public int type;

	@Column(name = "video_url")
		public String videoUrl;

	public static List<Video> getAll() {
		return new Select()
			.from(Video.class)
			.execute();
	}

	public static List<Video> getAllWithType(int type) {
		return new Select()
			.from(Video.class)
			.where("type = ?", type)
			.execute();
	}

	@Override
	public String toString() {
		return title;
	}
}
