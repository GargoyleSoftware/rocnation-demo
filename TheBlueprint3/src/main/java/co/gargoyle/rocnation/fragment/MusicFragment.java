package co.gargoyle.rocnation.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import co.gargoyle.rocnation.R;
import co.gargoyle.rocnation.events.MusicServiceConnectedEvent;
import co.gargoyle.rocnation.events.MusicTrackChangedEvent;
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
public class MusicFragment extends Fragment {

	@Inject Bus bus;

	private Handler mHandler = new Handler();
	private ArrayList<String> images;
	private Iterator<String> iterator;
	private View rootView;

	private MusicService mMusicService;

	private Button mTitleButton;

	@Inject
	public MusicFragment() {
	}

	////////////////////////////////////////////////////////////
	// Fragment Lifecycle
	////////////////////////////////////////////////////////////

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		bus.register(this);

		rootView = inflater.inflate(R.layout.fragment_music, container, false);

		mTitleButton = (Button) rootView.findViewById(R.id.title);
		mTitleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Herro", Toast.LENGTH_LONG).show();
				showAlert();
			}
		});

		getActivity().setTitle("Music");
		try {
			images = new ArrayList<String>(Arrays.asList(getActivity().getAssets().list("art")));
			iterator = images.iterator();
			setArtImage();
			mHandler.postDelayed(mUpdateTimeTask, 10000);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rootView;
	}

	public void onDestroyView () {
		super.onDestroyView();

		mHandler.removeCallbacks(mUpdateTimeTask);
		bus.unregister(this);
	}

	////////////////////////////////////////////////////////////
	// Fragment Lifecycle
	////////////////////////////////////////////////////////////

	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			setArtImage();
			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 5000);
		}
	};

	private void setArtImage() {
		ImageView imageView = (ImageView) rootView.findViewById(R.id.art);
		InputStream imageStream;
		try {
			if(!iterator.hasNext()) {
				iterator = images.iterator();
			}
			String filePath = "art/" + iterator.next();
			System.out.println("Setting Image to file: " + filePath);
			imageStream = getActivity().getAssets().open(filePath);
			imageView.setImageDrawable(Drawable.createFromStream(imageStream, null));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Select Track");

		ListView modeList = new ListView(getActivity());

		final List<Song> songs = Song.getAll();
		modeList.setAdapter(
				new ModelAdapter<Song>(
					getActivity(),
					android.R.layout.simple_list_item_activated_1,
					android.R.id.text1,
					songs));

		modeList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		int pos = mMusicService.getCurrentSongIndex();

		modeList.setItemChecked(pos, true);

		builder.setView(modeList);
		final Dialog dialog = builder.create();
		dialog.show();

		modeList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos,
					long id) {
				dialog.dismiss();

				Song song = songs.get(pos);
				Log.d("modeList", "item selected: " + song);
				bus.post(new MusicTrackRequestEvent(song));
			}
		});
	}

	////////////////////////////////////////////////////////////
	// Otto Events
	////////////////////////////////////////////////////////////

	@Subscribe
	public void onMusicServiceConnected(MusicServiceConnectedEvent event) {
		Log.d("otto", "musicService connected");

		MusicService musicService = event.service;

		mMusicService = musicService;
	}

	@Subscribe
	public void onMusicTrackChanged(MusicTrackChangedEvent event) {
		Log.d("otto-music", "musicTrackChanged: " + event.song);

		updateSongTitleIndicator(event.song);
	}

	////////////////////////////////////////////////////////////
	// Misc
	////////////////////////////////////////////////////////////

	private void updateSongTitleIndicator(Song song) {
		mTitleButton.setText(song.fullName());
	}

}
