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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.common.base.Optional;
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

	private ImageView mImageViewA;
	//private ImageView mImageViewB;
	private Animation myFadeInAnimation;
//	private Animation myFadeOutAnimation;

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

		mImageViewA = (ImageView) rootView.findViewById(R.id.image_a);
//		mImageViewB = (ImageView) rootView.findViewById(R.id.image_b);

		myFadeInAnimation  = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
//		myFadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);

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
			//mHandler.postDelayed(mUpdateTimeTask, 10000);
			mHandler.postDelayed(mUpdateTimeTask, 2000);
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
		Optional<Drawable> drawable = getNextDrawable();

		if (drawable.isPresent()) {
			animateImageViews(drawable.get());
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
	// Image loading
	////////////////////////////////////////////////////////////

	private Optional<Drawable> getNextDrawable() {
		try {
			if(!iterator.hasNext()) {
				iterator = images.iterator();
			}
			String filePath = "art/" + iterator.next();
			System.out.println("Setting Image to file: " + filePath);
			InputStream imageStream;
			imageStream = getActivity().getAssets().open(filePath);
			Drawable drawable = Drawable.createFromStream(imageStream, null);
			return Optional.of(drawable);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.absent();
	}

	private void animateImageViews(Drawable drawable) {
		// if (mImageViewA.getAlpha() == 1.0) {
		// 	swapImageViews(mImageViewA, mImageViewB, drawable);
		// } else {
		// 	swapImageViews(mImageViewB, mImageViewA, drawable);
		// }
		mImageViewA.setImageDrawable(drawable);
		mImageViewA.startAnimation(myFadeInAnimation);
	}

//	private void swapImageViews(ImageView oldie, ImageView newbie, Drawable drawable) {
//		// set new guy alpha to 0
//		//newbie.setAlpha(0);
//		// set his drawable
//		newbie.setImageDrawable(drawable);
//
//		// Fade out the old guy
//		//oldie.startAnimation(myFadeOutAnimation);
//		// Fade in the new guy
//		newbie.startAnimation(myFadeInAnimation);
//	}

	////////////////////////////////////////////////////////////
	// Misc
	////////////////////////////////////////////////////////////

	private void updateSongTitleIndicator(Song song) {
		mTitleButton.setText(song.fullName());
	}

}
