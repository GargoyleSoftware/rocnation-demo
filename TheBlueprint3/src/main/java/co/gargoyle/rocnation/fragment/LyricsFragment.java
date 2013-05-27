package co.gargoyle.rocnation.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.gargoyle.rocnation.R;



////////////////////////////////////////////////////////////
// TicketsFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class LyricsFragment extends Fragment {
	public static final int img = R.drawable.lyrics_view;

	public LyricsFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_lyrics, container, false);


//		ImageView iView = (ImageView) rootView.findViewById(R.id.image_view);

//		iView.setImageResource(img);
		
		// int i = getArguments().getInt(ARG_PLANET_NUMBER);
		// String planet = getResources().getStringArray(R.array.nav_array)[i];

		getActivity().setTitle("Lyrics");

		return rootView;
	}
}