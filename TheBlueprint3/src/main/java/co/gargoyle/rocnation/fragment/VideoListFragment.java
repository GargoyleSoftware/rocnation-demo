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
import co.gargoyle.rocnation.events.VideoRequestEvent;
import co.gargoyle.rocnation.model.Video;

import com.activeandroid.widget.ModelAdapter;
import com.squareup.otto.Bus;

public class VideoListFragment extends ListFragment {

	private int mMode;

	@Inject
	Bus bus;

	@Inject
	public VideoListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_video_list, container, false);

		getActivity().setTitle("Video");

		List<Video> videos = Video.getAllWithType(mMode);
		Log.d("videos", videos.toString());

		setListAdapter(
				new ModelAdapter<Video>(
						getActivity(),
						android.R.layout.simple_list_item_activated_1,
						android.R.id.text1,
						videos));

		return rootView;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		ModelAdapter<Video> videoAdapter = getModelAdapter();
		Video video = videoAdapter.getItem(position);

		playVideo(video, position);
		// play video at index
	}


	@SuppressWarnings("unchecked")
	private ModelAdapter<Video> getModelAdapter() {
		return (ModelAdapter<Video>) getListAdapter();
	}

	private void playVideo(Video video, int position) {
		bus.post(new VideoRequestEvent(video, position));
	}

	public int getMode() {
		return mMode;
	}

	public void setMode(int mMode) {
		this.mMode = mMode;
	}

}
