package co.gargoyle.rocnation.fragment;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import co.gargoyle.rocnation.R;
import co.gargoyle.rocnation.activity.MainActivity;
import co.gargoyle.rocnation.events.MusicTimeChangedEvent;
import co.gargoyle.rocnation.events.MusicTrackChangedEvent;
import co.gargoyle.rocnation.list.LyricAdapter;
import co.gargoyle.rocnation.model.Song;
import co.gargoyle.rocnation.model.json.Lyric;
import co.gargoyle.rocnation.service.MusicService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Optional;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;



////////////////////////////////////////////////////////////
// TicketsFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class LyricsFragment extends ListFragment {
	public static final int img = R.drawable.lyrics_view;

	@Inject Bus mBus;

	private int mCurrentIndex;
	private MusicService mMusicService;

	////////////////////////////////////////////////////////////
	// Constructors
	////////////////////////////////////////////////////////////

	@Inject
	public LyricsFragment() {
		// Empty constructor required for fragment subclasses
	}

	private Toast mCurrentToast;

	////////////////////////////////////////////////////////////
	// Fragment Lifecycle
	////////////////////////////////////////////////////////////

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setTitle("Lyrics");

		MainActivity activity = (MainActivity) getActivity();
		Optional<MusicService> musicOpt = activity.getMusicService();
		if (musicOpt.isPresent()) {
			mMusicService  = musicOpt.get();
			loadLyricsForSong(mMusicService.getCurrentSong());
		}

		Log.d("bus exists", mBus.toString());
		mBus.register(this);

		View rootView = inflater.inflate(R.layout.fragment_lyrics, container, false);

		// empire state

		return rootView;
	}

	public void onDestroyView () {
		super.onDestroyView();

		mBus.unregister(this);
	}

	////////////////////////////////////////////////////////////
	// ListFragment
	////////////////////////////////////////////////////////////

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Lyric lyric = getLyricAtIndex(position);

		showToastWithLyric(lyric);
	}

	////////////////////////////////////////////////////////////
	// Main Logic
	////////////////////////////////////////////////////////////

	private Lyric getLyricAtIndex(int position) {
		LyricAdapter lyricAdapter = (LyricAdapter) getListAdapter();
		Lyric lyric = (Lyric) lyricAdapter.getItem(position);

		return lyric;
	}

	private void showToastWithLyric(Lyric lyric) {
		Toast newToast = Toast.makeText(getActivity(), lyric.annotation, Toast.LENGTH_LONG);

		if (mCurrentToast != null) {
			mCurrentToast.cancel();
		}
		newToast.show();
		mCurrentToast = newToast;
	}

	private void advanceLyricIfNecessary(long millis) {
		// TODO: test this
		try {
			Lyric nextLyric = getLyricAtIndex(mCurrentIndex + 2);
			long nextTime = nextLyric.timestamp;
			if (millis >= nextTime) {
				// advance to next lyric
				setCurrentIndex(mCurrentIndex + 1);
			} else {
				// chill on our current lyric
			}
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	private void setCurrentIndex(int position) {
		mCurrentIndex = position;
		getListView().setSelection(mCurrentIndex);

		// TODO: Move listview to this index
	}

	private void loadLyricsForSong(Song song) {
		if (song != null) {
			ObjectMapper mapper = new ObjectMapper();

			TypeFactory typeFactory = mapper.getTypeFactory();
			String lyricJsonString = song.lyricJson;
			JavaType lyricsType = typeFactory.constructCollectionType(List.class, Lyric.class);

			List<Lyric> lyrics = null;
			try {
				lyrics = mapper.readValue(lyricJsonString, lyricsType);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (lyrics == null) {
				// don't make a list adapter
			} else {
				setListAdapter(new LyricAdapter(getActivity(), lyrics));
			}
		}
	}

	////////////////////////////////////////////////////////////
	// Otto Events
	////////////////////////////////////////////////////////////

	@Subscribe
	public void musicTimeChanged(MusicTimeChangedEvent event) {
		Log.d("lyrics", "musicTimeChanged: " + String.valueOf(event.currentTime));

		advanceLyricIfNecessary(event.currentTime);
		// int newIndex = getIndexByTime(event.currentTime);
		// setCurrentIndex(newIndex);
	}

	@Subscribe
	public void onMusicTrackChanged(MusicTrackChangedEvent event) {
		Log.d("otto-lyrics", "musicTrackChanged: " + event.song);


		loadLyricsForSong(event.song);
	}

}
