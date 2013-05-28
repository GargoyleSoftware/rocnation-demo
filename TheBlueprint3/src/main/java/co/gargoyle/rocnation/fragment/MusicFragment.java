package co.gargoyle.rocnation.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.inject.Inject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import co.gargoyle.rocnation.R;
import com.squareup.otto.Bus;

////////////////////////////////////////////////////////////
// MusicFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class MusicFragment extends Fragment {

	@Inject
	Bus bus;
	
	private Handler mHandler = new Handler();
	private ArrayList<String> images;
	private Iterator<String> iterator;
	private View rootView;

	@Inject
	public MusicFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		bus.register(this);

		rootView = inflater.inflate(R.layout.fragment_music, container, false);

		Button button = (Button) rootView.findViewById(R.id.title);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Herro", Toast.LENGTH_LONG).show();
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
	
	
}
