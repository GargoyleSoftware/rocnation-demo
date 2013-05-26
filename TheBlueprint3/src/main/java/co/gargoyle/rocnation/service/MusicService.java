package co.gargoyle.rocnation.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import co.gargoyle.rocnation.R;

public class MusicService extends Service implements MediaPlayer.OnErrorListener {

    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mMediaPlayer;
    private int length = 0;

    ////////////////////////////////////////////////////////////
    // ServiceBinder
    ////////////////////////////////////////////////////////////

    public class ServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    ////////////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////////////

    public MusicService() { 
    	
    }

    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }

    ////////////////////////////////////////////////////////////
    // Service Lifecyle
    ////////////////////////////////////////////////////////////

    @Override
    public void onCreate () {
        super.onCreate();

        Log.d("service", "onCreate");

        mMediaPlayer = MediaPlayer.create(this, R.raw.jingle);
        mMediaPlayer.setOnErrorListener(this);

        if(mMediaPlayer!= null) {
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setVolume(100,100);
        }

        mMediaPlayer.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                onError(mMediaPlayer, what, extra);
                return true;
            }
        });
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        Log.d("service", "onDestroy");
        if(mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } finally {
                mMediaPlayer = null;
            }
        }
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        Log.d("service", "onStartCommand");
        mMediaPlayer.start();
        return START_STICKY;
    }

    ////////////////////////////////////////////////////////////
    // Play Controls
    ////////////////////////////////////////////////////////////

    public void pauseMusic() {
        Log.d("service", "pauseMusic");
        if(mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            length=mMediaPlayer.getCurrentPosition();
        }
    }

    public void resumeMusic() {
        Log.d("service", "resumeMusic");
        if(mMediaPlayer.isPlaying()==false) {
            mMediaPlayer.seekTo(length);
            mMediaPlayer.start();
        }
    }

    public void stopMusic() {
        Log.d("service", "stopMusic");
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d("service", "onError");
        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if(mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } finally {
                mMediaPlayer = null;
            }
        }
        return false;
    }
}
