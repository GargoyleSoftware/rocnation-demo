package co.gargoyle.rocnation.fragment;

import java.io.IOException;
import java.util.List;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import co.gargoyle.rocnation.R;
import co.gargoyle.rocnation.dummy.DummyContent;
import co.gargoyle.rocnation.list.LyricAdapter;
import co.gargoyle.rocnation.model.Song;
import co.gargoyle.rocnation.model.json.Lyric;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;



////////////////////////////////////////////////////////////
// TicketsFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class LyricsFragment extends ListFragment {
    public static final int img = R.drawable.lyrics_view;

    public LyricsFragment() {
        // Empty constructor required for fragment subclasses
    }

    private Toast mCurrentToast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        getActivity().setTitle("Lyrics");

        View rootView = inflater.inflate(R.layout.fragment_lyrics, container, false);

        // empire state
        Song song = Song.getByTrack(5);


        ObjectMapper mapper = new ObjectMapper();

        TypeFactory typeFactory = mapper.getTypeFactory();
        String lyricJsonString = song.lyricJson;
        JavaType lyricsType = typeFactory.constructCollectionType(List.class, Lyric.class);

        List<Lyric> lyrics = null;
        try {
            lyrics = mapper.readValue(lyricJsonString, lyricsType);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lyrics == null) {
            // don't make a list adapter

        } else {
            setListAdapter(new LyricAdapter(getActivity(), lyrics));
        }


        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        LyricAdapter lyricAdapter = (LyricAdapter) getListAdapter();
        Lyric lyric = (Lyric) lyricAdapter.getItem(position);

        showToastWithLyric(lyric);


    }

    private void showToastWithLyric(Lyric lyric) {

        Toast newToast = Toast.makeText(getActivity(), lyric.annotation, Toast.LENGTH_LONG);

        if (mCurrentToast != null) {
            mCurrentToast.cancel();
        }
        newToast.show();
        mCurrentToast = newToast;

    }
}
