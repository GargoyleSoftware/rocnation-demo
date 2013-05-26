package co.gargoyle.rocnation.fragment;

import java.util.List;

import javax.inject.Inject;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import co.gargoyle.rocnation.R;
import co.gargoyle.rocnation.dummy.DummyContent;
import co.gargoyle.rocnation.model.Song;

import com.squareup.otto.Bus;



////////////////////////////////////////////////////////////
// MusicFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class MusicFragment extends ListFragment {

	@Inject
	Bus bus;

	// @Inject DatabaseHelper databaseHelper;

	@Inject
    public MusicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_music, container, false);

        getActivity().setTitle("Music");


		List<Song> songs = Song.getAll();
		Log.d("songs", songs.toString());


		// databaseHelper.openDatabase();
		// Cursor songs = databaseHelper.getAllSongs();

        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                DummyContent.ITEMS));



        return rootView;
    }
}
