package co.gargoyle.rocnation.fragment;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import co.gargoyle.rocnation.R;



////////////////////////////////////////////////////////////
// PlanetFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class PlanetFragment extends Fragment {
	public static final String ARG_PLANET_NUMBER = "planet_number";

	public PlanetFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
		Bundle arguments = getArguments();
    int i;
		if (arguments != null) {
			i = getArguments().getInt(ARG_PLANET_NUMBER);
		} else {
			i = 0;
		}

		String planet = getResources().getStringArray(R.array.nav_array)[i];

		int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
						"drawable", getActivity().getPackageName());
		((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
		getActivity().setTitle(planet);
		return rootView;
	}
}
