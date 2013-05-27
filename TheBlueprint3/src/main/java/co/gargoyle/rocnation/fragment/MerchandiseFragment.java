package co.gargoyle.rocnation.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.gargoyle.rocnation.R;



////////////////////////////////////////////////////////////
// MerchandiseFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class MerchandiseFragment extends Fragment {
	public static final int img = R.drawable.merch_view;

	public MerchandiseFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_merchandise, container, false);

		// int i = getArguments().getInt(ARG_PLANET_NUMBER);
		// String planet = getResources().getStringArray(R.array.nav_array)[i];

		getActivity().setTitle("Merchandise");

		return rootView;
	}
}
