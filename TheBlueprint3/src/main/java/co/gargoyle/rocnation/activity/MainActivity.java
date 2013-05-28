package co.gargoyle.rocnation.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.inject.Inject;
import javax.inject.Named;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost;
import android.widget.VideoView;
import co.gargoyle.rocnation.R;
import co.gargoyle.rocnation.RocApplication;
import co.gargoyle.rocnation.constants.PlaybackMode;
import co.gargoyle.rocnation.events.MusicPausedEvent;
import co.gargoyle.rocnation.events.MusicPlayingEvent;
import co.gargoyle.rocnation.events.MusicServiceConnectedEvent;
import co.gargoyle.rocnation.events.MusicTimeChangedEvent;
import co.gargoyle.rocnation.events.MusicTrackChangedEvent;
import co.gargoyle.rocnation.events.MusicTrackRequestEvent;
import co.gargoyle.rocnation.events.VideoRequestEvent;
import co.gargoyle.rocnation.fragment.LyricsFragment;
import co.gargoyle.rocnation.fragment.MerchandiseFragment;
import co.gargoyle.rocnation.fragment.MusicFragment;
import co.gargoyle.rocnation.fragment.PlanetFragment;
import co.gargoyle.rocnation.fragment.TicketsFragment;
import co.gargoyle.rocnation.fragment.VideoListFragment;
import co.gargoyle.rocnation.list.NavAdapter;
import co.gargoyle.rocnation.model.Video;
import co.gargoyle.rocnation.service.MusicService;
import co.gargoyle.rocnation.tabs.PagerAdapter;
import co.gargoyle.rocnation.tabs.TabFactory;
import co.gargoyle.rocnation.tabs.TabInfo;

import com.androidhive.musicplayer.MusicTimeUtilities;
import com.google.common.base.Optional;
import com.squareup.otto.Subscribe;

/**
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 */
public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
	private DrawerLayout mDrawerLayout;
	private ListView mNavDrawerList;

	private TabHost mVideoTabHost;
	private ViewGroup mVideoDrawer;

	private ActionBarDrawerToggle mDrawerToggle;

	private FrameLayout mContentFrame;
	private RelativeLayout mVideoFrame;
	private RelativeLayout mPlayerFrame;
	private HashMap<String, TabInfo> mVideoTabInfo = new HashMap<String, TabInfo>();
	private PagerAdapter mVideoPagerAdapter;
	private ViewPager mVideoViewPager;

	private VideoView mVideoView;

	private ImageButton mPlayButton;
	private ImageButton mRewindButton;
	private ImageButton mFfwdButton;
	private SeekBar mSongProgressBar;
	// private MusicService.ServiceBinder mPlaybackBinder;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mNavTitles;

	private boolean mIsBound = false;
	private MusicService mServ;

	private boolean mVideoMode = false;

	@Inject com.squareup.otto.Bus mBus;
	@Inject MusicFragment mMusicFragment;
	@Inject TicketsFragment mTicketsFragment;
	@Inject LyricsFragment mLyricsFragment;
	@Inject MusicTimeUtilities mMusicTimeUtilities;

	@Inject @Named("music")VideoListFragment mVideoFrag1;
	@Inject @Named("interview")VideoListFragment mVideoFrag2;
	@Inject @Named("making")VideoListFragment mVideoFrag3;

	////////////////////////////////////////////////////////////
	// Constructor
	////////////////////////////////////////////////////////////

	@Inject
	public MainActivity() {
	}

	////////////////////////////////////////////////////////////
	// ServiceConnection
	////////////////////////////////////////////////////////////

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder
				binder) {
			MusicService.ServiceBinder musicBinder = (MusicService.ServiceBinder)binder;
			mServ = musicBinder.getService();

			mBus.post(new MusicServiceConnectedEvent(mServ));
		}

		public void onServiceDisconnected(ComponentName name) {
			mServ = null;
		}
	};

	void doBindService(){
		Intent playMusicIntent = new Intent("co.gargoyle.rocnation.intent.action.PLAY");

		boolean result = bindService(playMusicIntent, mServiceConnection,Context.BIND_AUTO_CREATE);
		if (result) {
			mIsBound = true;
		}
	}

	void doUnbindService() {
		if(mIsBound) {
			unbindService(mServiceConnection);
			mIsBound = false;
		}
	}

	////////////////////////////////////////////////////////////
	// Activity Lifecycle
	////////////////////////////////////////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RocApplication app = (RocApplication) getApplication();
		app.getApplicationGraph().inject(this);
		mBus.register(this);

		mTitle = mDrawerTitle = getTitle();
		mNavTitles = getResources().getStringArray(R.array.nav_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mNavDrawerList = (ListView) findViewById(R.id.nav_drawer);

		mVideoDrawer = (ViewGroup) findViewById(R.id.video_drawer);
		mVideoTabHost = (TabHost) findViewById(R.id.video_drawer);

		initTabHost(savedInstanceState);
		initViewPager();
		//mVideoTabHost.setup();

		//mVideoDrawer = (ViewGroup) findViewById(R.id.video_wrapper);
		//		mVideoList = (ListView) findViewById(R.id.video_list);

		mContentFrame = (FrameLayout) findViewById(R.id.content_frame);
		mVideoFrame   = (RelativeLayout) findViewById(R.id.video_frame);
		mPlayerFrame  = (RelativeLayout) findViewById(R.id.player_frame);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

		mVideoView   = (VideoView) findViewById(R.id.top_video);
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(mVideoView);
		// Set video link (mp4 format )
		mVideoView.setMediaController(mediaController);

		mPlayButton = (ImageButton) findViewById(R.id.play_button);
		mPlayButton.setOnClickListener(mOnPlayPressedListener);
		mRewindButton = (ImageButton) findViewById(R.id.rewind_button);
		mRewindButton.setOnClickListener(mOnRewindPressedListener);
		mFfwdButton = (ImageButton) findViewById(R.id.ffwd_button);
		mFfwdButton.setOnClickListener(mOnFfwdPressedListener);

		mSongProgressBar = (SeekBar) findViewById(R.id.song_progress_bar);
		mSongProgressBar.setOnSeekBarChangeListener(mSongSeekBarListener);

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mNavDrawerList.setAdapter(new NavAdapter(this));
		mNavDrawerList.setOnItemClickListener(new DrawerItemClickListener());

//		List<Video> videos = Video.getAll();
//		mVideoDrawerList.setAdapter(
//				new ModelAdapter<Video>(
//						this,
//						android.R.layout.simple_list_item_activated_1,
//						android.R.id.text1,
//						videos));
//		mVideoDrawerList.setOnItemClickListener(mVideoDrawerListener);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

		doBindService();

		//ViewServer.get(this).addWindow(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		doUnbindService();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	////////////////////////////////////////////////////////////
	// Menu
	////////////////////////////////////////////////////////////

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
//		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mNavDrawerList);
		//menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);

		menu.findItem(R.id.action_video).setVisible(mVideoMode);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch(item.getItemId()) {

		// case R.id.action_websearch:
		// 	// create intent to perform web search for this planet
		// 	Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
		// 	intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
		// 	// catch event that there's no activity to handle intent
		// 	if (intent.resolveActivity(getPackageManager()) != null) {
		// 		startActivity(intent);
		// 	} else {
		// 		Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
		// 	}
		// 	return true;
		case R.id.action_video:
			mDrawerLayout.openDrawer(Gravity.RIGHT);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	////////////////////////////////////////////////////////////
	// Activity
	////////////////////////////////////////////////////////////

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

    ////////////////////////////////////////////////////////////
    // Listeners
    ////////////////////////////////////////////////////////////

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

	////////////////////////////////////////////////////////////
	// Nav
	////////////////////////////////////////////////////////////

	private void selectItem(int position) {
		navToPosition(position);

		// update selected item and title, then close the drawer
		updateSelectedItemAndCloseDrawer(position);
	}

	private void navToPosition(int position) {
		if (position == 1) {
			enterVideoMode();
		} else {
			exitVideoMode();

			// update the main content by replacing fragments
			Fragment fragment = getFragmentForPosition(position);
			swapFragment(fragment);
		}
	}

	private void updateSelectedItemAndCloseDrawer(int position) {
		mNavDrawerList.setItemChecked(position, true);
		setTitle(mNavTitles[position]);
		mDrawerLayout.closeDrawer(mNavDrawerList);
	}

	private Fragment getFragmentForPosition(int position) {
		Fragment fragment;

		switch (position) {
		case 0:
			fragment = mMusicFragment;
			break;
		case 1:
			fragment = new PlanetFragment();
			break;
		case 2:
			fragment = mTicketsFragment;
			break;
		case 3:
			fragment = new MerchandiseFragment();
			break;
		case 4:
			fragment = mLyricsFragment;
			break;
		default:
			fragment = new PlanetFragment();
			Bundle args = new Bundle();
			args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
			fragment.setArguments(args);
			break;
		}

		return fragment;
	}

	private void swapFragment(Fragment fragment) {
		FragmentManager fm = getSupportFragmentManager();
		for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
			fm.popBackStack();
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	}

	////////////////////////////////////////////////////////////
	// Music
	////////////////////////////////////////////////////////////

	View.OnClickListener mOnPlayPressedListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Log.d("activity", "onPlayPressed");

			mServ.toggleMusic();
		}
	};

	View.OnClickListener mOnRewindPressedListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Log.d("activity", "onRewindPressed");

			try {
				mServ.toPreviousTrack();
			} catch (IOException e) {
				mServ.toggleMusic();
				e.printStackTrace();
			}
		}
	};

	View.OnClickListener mOnFfwdPressedListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Log.d("activity", "onFforwardPressed");

			try {
				mServ.toNextTrack();
			} catch (IOException e) {
				mServ.toggleMusic();
				e.printStackTrace();
			}
		}
	};

  private void updateMusicButton(PlaybackMode mode) {
    if (mode == PlaybackMode.PAUSED) {
      mPlayButton.setImageResource(R.drawable.player_play_button);
    } else if (mode == PlaybackMode.PLAYING) {
      mPlayButton.setImageResource(R.drawable.player_pause_button);
    }
  }

  private OnSeekBarChangeListener mSongSeekBarListener = new OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    	if (fromUser) {
    		Log.v("seek", "user");
    		int newTime = progress;
    		mServ.seek(newTime);
    		//mBus.post(new MusicTimeRequestEvent(newTime));
    	} else {
    		Log.v("seek", "not user");
    	}
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
      // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
      // TODO Auto-generated method stub

    }

  };

	////////////////////////////////////////////////////////////
	// Video Player
	////////////////////////////////////////////////////////////

    private void updateSelectedVideoAndCloseDrawer(int position, Video video) {
//        mVideoDrawerList.setItemChecked(position, true);
        setTitle(video.title);
        mDrawerLayout.closeDrawer(mVideoDrawer);
    }

    private void enterVideoMode() {
      mVideoFrame.setVisibility(View.VISIBLE);

      mPlayerFrame.setVisibility(View.GONE);
      mContentFrame.setVisibility(View.GONE);

      mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);

			// pause music
			mServ.pauseMusic();

      mVideoMode = true;
    }

    private void exitVideoMode() {
        pauseVideo();

        mVideoFrame.setVisibility(View.GONE);

        mPlayerFrame.setVisibility(View.VISIBLE);
        mContentFrame.setVisibility(View.VISIBLE);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

        mVideoMode = false;
    }

    private void pauseVideo() {
        mVideoView.pause();
    }

    private void playVideo(Video video) {
        mVideoView.stopPlayback();
        mVideoView.setVideoURI(Uri.parse(video.videoUrl));
        mVideoView.start();
    }

    ////////////////////////////////////////////////////////////
    // Otto Subscriptions
    ////////////////////////////////////////////////////////////

    @Subscribe
    public void musicTimeChanged(MusicTimeChangedEvent event) {
      Log.d("otto", "musicTimeChanged: " + String.valueOf(event.currentTime));

      //		songTotalDurationLabel.setText(""
      //				+ utils.milliSecondsToTimer(event.totalTime));
      //		songCurrentDurationLabel.setText(""
      //				+ utils.milliSecondsToTimer(event.currentTime));

      // Updating progress bar
      //int progress = (int) (mMusicTimeUtilities.getProgressPercentage(event.currentTime, event.totalTime));

      //    mSongProgressBar.getMax()
      //    mSongProgressBar.setMax(event.totalTime)

      int castedTime = (int) event.currentTime;
      mSongProgressBar.setProgress(castedTime);
      Log.d("progress", "setting progress to: " + castedTime);
    }

    @Subscribe
    public void onMusicPlaying(MusicPlayingEvent event) {
      Log.d("otto", "onMusicPlaying");

      updateMusicButton(PlaybackMode.PLAYING);
    }

    @Subscribe
    public void onMusicPaused(MusicPausedEvent event) {
      Log.d("otto", "onMusicPaused");

      updateMusicButton(PlaybackMode.PAUSED);
    }

    @Subscribe
    public void onMusicTrackRequest(MusicTrackRequestEvent event) {
      Log.d("otto", "musicTrackRequest: " + event.song);

      try {
        mServ.playSong(event.song);
        // updateMusicButton(PlaybackMode.PLAYING);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Subscribe
    public void onMusicTrackChanged(MusicTrackChangedEvent event) {
      Log.d("otto", "musicTrackChanged: " + event.song);

      mSongProgressBar.setProgress(0);
      Log.d("progress", "setting progress to 0");

      int castedMax = (int) event.totalTime;
      Log.d("progress", "setting max to: " + castedMax);
      mSongProgressBar.setMax(castedMax);
    }

    @Subscribe
    public void onVideoRequest(VideoRequestEvent event) {
      Log.d("otto", "videoRequest: " + event.video);

      updateSelectedVideoAndCloseDrawer(event.index, event.video);
      playVideo(event.video);
    }

    ////////////////////////////////////////////////////////////
    // TabHost
    ////////////////////////////////////////////////////////////

    /**
     * Initialise ViewPager
     */
    private void initViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
        // fragments.add(Fragment.instantiate(this, PlanetFragment.class.getName()));
        // fragments.add(Fragment.instantiate(this, PlanetFragment.class.getName()));
        // fragments.add(Fragment.instantiate(this, PlanetFragment.class.getName()));

        fragments.add(mVideoFrag1);
        fragments.add(mVideoFrag2);
        fragments.add(mVideoFrag3);

        this.mVideoPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);

        this.mVideoViewPager = (ViewPager)super.findViewById(R.id.video_section_pager);
        this.mVideoViewPager.setAdapter(this.mVideoPagerAdapter);
        this.mVideoViewPager.setOnPageChangeListener(this);
    }

    /**
     * Initialise the Tab Host
     */
    private void initTabHost(Bundle args) {
        mVideoTabHost.setup();
        TabInfo tabInfo = null;
        MainActivity.AddTab(this, this.mVideoTabHost, this.mVideoTabHost.newTabSpec("Music").setIndicator("Music Videos"), ( tabInfo = new TabInfo("Music", PlanetFragment.class, args)));
        this.mVideoTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(this, this.mVideoTabHost, this.mVideoTabHost.newTabSpec("Interviews").setIndicator("Inter-\nviews"), ( tabInfo = new TabInfo("Interviews", PlanetFragment.class, args)));
        this.mVideoTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(this, this.mVideoTabHost, this.mVideoTabHost.newTabSpec("Making").setIndicator("Making Of"), ( tabInfo = new TabInfo("Making", PlanetFragment.class, args)));
        this.mVideoTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mVideoTabHost.setOnTabChangedListener(this);
    }

    private static void AddTab(MainActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    @Override
    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mVideoTabInfo.get(tag);
        int pos = this.mVideoTabHost.getCurrentTab();

        this.mVideoViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
      mVideoTabHost.setCurrentTab(position);
    }

    ////////////////////////////////////////////////////////////
    // Misc
    ////////////////////////////////////////////////////////////

    public Optional<MusicService> getMusicService() {
    	if (mServ == null) {
    		return Optional.absent();
    	} else {
    		return Optional.of(mServ);
    	}
    }
}
