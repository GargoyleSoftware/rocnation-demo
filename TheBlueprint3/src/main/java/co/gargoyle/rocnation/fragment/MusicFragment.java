package co.gargoyle.rocnation.fragment;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

	@Inject
	public MusicFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		bus.register(this);

		View rootView = inflater.inflate(R.layout.fragment_music, container, false);

		Button button = (Button) rootView.findViewById(R.id.title);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Herro", Toast.LENGTH_LONG).show();
			}
		});
		
		getActivity().setTitle("Music");

		return rootView;
	}
	
	
}
