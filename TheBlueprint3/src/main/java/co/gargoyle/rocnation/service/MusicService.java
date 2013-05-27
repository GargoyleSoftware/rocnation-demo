package co.gargoyle.rocnation.service;

import javax.inject.Inject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import co.gargoyle.rocnation.RocApplication;
import co.gargoyle.rocnation.events.MusicPausedEvent;
import co.gargoyle.rocnation.events.MusicPlayingEvent;
import co.gargoyle.rocnation.events.MusicTrackChangeEvent;
import co.gargoyle.rocnation.model.Song;

import com.squareup.otto.Subscribe;

public class MusicService extends Service implements MediaPlayer.OnErrorListener {

    @Inject com.squareup.otto.Bus bus;

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

    @Inject
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

        RocApplication app = (RocApplication) getApplication();
        app.getApplicationGraph().inject(this);

        Log.d("service", "onCreate");

        // Uri uri = Uri.parse("https://gargoyle.s3.amazonaws.com/rocnation/audio/01-What%20We%20Talkin%20About.mp3");
        Uri uri = Uri.parse("https://gargoyle.s3.amazonaws.com/rocnation/audio/03-DOA.mp3");
        mMediaPlayer = MediaPlayer.create(app, uri);

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

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

	public void playSong(Song song) throws java.io.IOException {
        Log.d("service", "playSong: " + song);

		mMediaPlayer.stop();
		mMediaPlayer.reset();


		mMediaPlayer.setDataSource(song.audioUrl);
		mMediaPlayer.prepare();
		mMediaPlayer.start();
	}

    public void toggleMusic() {
        if (isPlaying()) {
            pauseMusic();
        } else {
            resumeMusic();
        }
    }

    public void pauseMusic() {
        Log.d("service", "pauseMusic");
        if(mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            length = mMediaPlayer.getCurrentPosition();

            bus.post(new MusicPausedEvent());
        }
    }

    public void resumeMusic() {
        Log.d("service", "resumeMusic");
        if(mMediaPlayer.isPlaying() == false) {
            mMediaPlayer.seekTo(length);
            mMediaPlayer.start();

            bus.post(new MusicPlayingEvent());
        }
    }

    public void stopMusic() {
        Log.d("service", "stopMusic");
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;

        bus.post(new MusicPausedEvent());
    }

	////////////////////////////////////////////////////////////
    // Bus Events
    ////////////////////////////////////////////////////////////

	@Subscribe
    public void onMusicTrackChanged(MusicTrackChangeEvent event) {
        Log.d("otto-service", "musicTrackChanged: " + event.song);
    }

    ////////////////////////////////////////////////////////////
    // Callbacks
    ////////////////////////////////////////////////////////////

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
