package co.gargoyle.rocnation.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;
import co.gargoyle.rocnation.R;



////////////////////////////////////////////////////////////
// VideoFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class VideoFragment extends Fragment implements SurfaceHolder.Callback {
  private static final String VIDEO_URL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";

  private VideoView mVideoView;
  // Put in your Video URL here
  // Declare some variables
  private ProgressDialog mProgressDialog;

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

      getActivity().setTitle("Video");

      View rootView = inflater.inflate(R.layout.fragment_video, container, false);
      //mVideoView = (VideoView) rootView.findViewById(R.id.video);
      mVideoView = (VideoView) rootView;

      //mVideoView.setVideoPath("http://commonsware.com/misc/test2.3gp");
      mVideoView.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
      mVideoView.start();

      // SurfaceHolder holder = mVideoView.getHolder();
      // holder.addCallback(this);
      // holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


      // playStreamingVideo();
      // Execute StreamVideo AsyncTask

      return rootView;
  }

//  private void playStreamingVideo() {
//    // String path="http://www.ted.com/talks/download/video/8584/talk/761";
//    String path1="http://commonsware.com/misc/test2.3gp";
//
//    // String path2 = "https://s3.amazonaws.com/gargoyle/rocnation/video/03-DOA.mp4";
//
//    //String bunny = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//
//
//    Uri uri = Uri.parse(path1);
//
//    mVideoView.setVideoURI(uri);
//    mVideoView.start();
//  }

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

//  private String makeAbsolutePath(String relativePath) {
//    String absolutePath = "";
//    // String[] Array of strings, one for each asset. These file names are relative to 'path'. You can open the file by concatenating 'path' and a name in the returned string (via File) and passing that to open().
//
//    return absolutePath;
//  }

  // StreamVideo AsyncTask
  private class StreamVideo extends AsyncTask<Void, Void, Void> {
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      // Create a progressbar
      mProgressDialog = new ProgressDialog(getActivity());
      // Set progressbar title
      mProgressDialog.setTitle("Android Video Streaming Tutorial");
      // Set progressbar message
      mProgressDialog.setMessage("Buffering...");
      mProgressDialog.setIndeterminate(false);
      // Show progressbar
      mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
      return null;
    }

    @Override
    protected void onPostExecute(Void args) {
      try {
        // Start the MediaController
        //MediaController mediaController = new MediaController(getActivity());
        MediaController mediaController = new MediaController(getActivity().getApplicationContext());
        mediaController.setAnchorView(mVideoView);

        mVideoView.setOnErrorListener(onErrorListener);
        
        mVideoView.setOnPreparedListener(onPreparedListener);
        mVideoView.setOnCompletionListener(onCompletionListener);

        // Get the URL from String VideoURL
        Uri video = Uri.parse(VIDEO_URL);
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoURI(video);

        mVideoView.requestFocus();

      } catch (Exception e) {
        mProgressDialog.dismiss();
        Log.e("Error", e.getMessage());
        e.printStackTrace();
      }
    }
  }

  private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
    // Close the progress bar and play the video
    public void onPrepared(MediaPlayer mp) {
      Log.d("mediaPlayer", "onPrepared");

      getActivity().runOnUiThread(new Runnable(){
        public void run(){
          mProgressDialog.dismiss();
          mVideoView.start();
        }
      });

    }
  };
  private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {

      @Override
      public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
          Log.d("mediaPlayer", String.format("onError: %d, %d", i, i2));
          return false;
      }
  };
  
//  private MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {
//
//    @Override
//    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
//      Log.d("mediaPlayer", String.format("onInfo: %d, %d", i, i2));
//      return false;
//    }
//  };
  
  private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
      Log.d("mediaPlayer", "onCompletion");
    }
  };

  @Override
  public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    Log.d("surface", String.format("surfaceChanged: %d, %d, %d", i, i2, i3));
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    Log.d("surface", "surfaceDestroyed");
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    new StreamVideo().execute();
  }
}
