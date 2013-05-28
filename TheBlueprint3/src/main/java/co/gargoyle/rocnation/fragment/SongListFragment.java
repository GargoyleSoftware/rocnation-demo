package co.gargoyle.rocnation.fragment;

import java.util.List;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import co.gargoyle.rocnation.R;
import co.gargoyle.rocnation.events.MusicServiceConnectedEvent;
import co.gargoyle.rocnation.events.MusicTrackRequestEvent;
import co.gargoyle.rocnation.model.Song;
import co.gargoyle.rocnation.service.MusicService;

import com.activeandroid.widget.ModelAdapter;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

////////////////////////////////////////////////////////////
// MusicFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class SongListFragment extends ListFragment {

	@Inject
	Bus bus;

  @Inject
  public SongListFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

		bus.register(this);

    View rootView = inflater.inflate(R.layout.fragment_song_list, container, false);

    getActivity().setTitle("Music");

    return rootView;
  }

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		ModelAdapter<Song> songAdapter = getModelAdapter();
		Song song = songAdapter.getItem(position);

		playSong(song);
	}

	@SuppressWarnings("unchecked")
	private ModelAdapter<Song> getModelAdapter() {
		return (ModelAdapter<Song>) getListAdapter();
	}

	private void playSong(Song song) {
		bus.post(new MusicTrackRequestEvent(song));
	}

	@Subscribe
	public void onMusicServiceConnected(MusicServiceConnectedEvent event) {
		Log.d("otto", "musicService connected");

		MusicService musicService = event.service;

		List<Song> songs = musicService.getSongs();
		Log.d("songs", songs.toString());
		setListAdapter(
				new ModelAdapter<Song>(
					getActivity(),
					android.R.layout.simple_list_item_activated_1,
					android.R.id.text1,
					songs));
	}

}
