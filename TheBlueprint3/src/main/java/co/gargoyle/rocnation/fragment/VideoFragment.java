package co.gargoyle.rocnation.fragment;

import java.util.Locale;
import java.util.HashMap;
import java.util.ArrayList;
import android.view.SurfaceHolder;
import android.media.MediaPlayer;
import android.widget.VideoView;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.content.res.AssetFileDescriptor;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import co.gargoyle.rocnation.R;



////////////////////////////////////////////////////////////
// VideoFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class VideoFragment extends ListFragment {

    private VideoView mVideoView;

	public VideoFragment() {
        // 03-DOA.mp4          05-EmpireStateofMind.mp4  15-YoungForever.mp4
        // 04-RunThisTown.mp4  07-OnToTheNextOne.mp4

		// Empty constructor required for fragment subclasses
        // mPlanetTitles = getResources().getStringArray(R.array.nav_array);
        // setListAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_video, container, false);

		getActivity().setTitle("Video");


        mVideoView = (VideoView) rootView.findViewById(R.id.video);
        SurfaceHolder holder = mVideoView.getHolder();
        // holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        MediaPlayer player = new MediaPlayer();
        player.setDisplay(holder);
        AssetFileDescriptor afd;
        try {
            afd = getActivity().getAssets().openFd("video/03-DOA.mp4");
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength());
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

	//public ArrayList<HashMap<String, String>> getPlayList(){

    //    for (String relativePath : assetManager.list("videos")) {
    //        HashMap<String, String> song = new HashMap<String, String>();

    //        String songTitle = relativePath.substring(0, (file.getName().length() - 4));

    //        song.put("songTitle", songTitle);
    //        song.put("songPath", makeAbsolutePath(relativePath));

    //        // Adding each song to SongList
    //        songsList.add(song);
    //    }
	//	// return songs list array
	//	return songsList;
	//}

    private String makeAbsolutePath(String relativePath) {
        String absolutePath = "";
// String[] Array of strings, one for each asset. These file names are relative to 'path'. You can open the file by concatenating 'path' and a name in the returned string (via File) and passing that to open().

        return absolutePath;
    }

}
