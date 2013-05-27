package co.gargoyle.rocnation.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
import co.gargoyle.rocnation.R;



////////////////////////////////////////////////////////////
// VideoFragment
////////////////////////////////////////////////////////////

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class VideoFragment extends Fragment implements SurfaceHolder.Callback {
  // private static final String VIDEO_URL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";

  private VideoView mVideoView;
  // Put in your Video URL here
  // Declare some variables
//  private ProgressDialog mProgressDialog;

  public VideoFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

      getActivity().setTitle("Video");

      View rootView = inflater.inflate(R.layout.fragment_video, container, false);
      mVideoView = (VideoView) rootView.findViewById(R.id.video);
      //mVideoView = (VideoView) rootView;


	  basicStream();

      return rootView;
  }

  private void basicStream() {
      //mVideoView.setVideoPath("http://commonsware.com/misc/test2.3gp");
      // mVideoView.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
      mVideoView.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
      mVideoView.start();
  }

//  private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
//    // Close the progress bar and play the video
//    public void onPrepared(MediaPlayer mp) {
//      Log.d("mediaPlayer", "onPrepared");
//
//      getActivity().runOnUiThread(new Runnable(){
//        public void run(){
//          mProgressDialog.dismiss();
//          mVideoView.start();
//        }
//      });
//
//    }
//  };
//  private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
//
//      @Override
//      public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
//          Log.d("mediaPlayer", String.format("onError: %d, %d", i, i2));
//          return false;
//      }
//  };

//  private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
//    @Override
//    public void onCompletion(MediaPlayer mediaPlayer) {
//      Log.d("mediaPlayer", "onCompletion");
//    }
//  };

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
    Log.d("surface", "surfaceCreated");

  }
}
