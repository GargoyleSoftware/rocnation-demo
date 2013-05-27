package co.gargoyle.rocnation.activity;

import java.io.IOException;

import javax.inject.Inject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import co.gargoyle.rocnation.R;
import co.gargoyle.rocnation.RocApplication;
import co.gargoyle.rocnation.events.MusicPausedEvent;
import co.gargoyle.rocnation.events.MusicPlayingEvent;
import co.gargoyle.rocnation.events.MusicTimeChangedEvent;
import co.gargoyle.rocnation.events.MusicTrackChangeEvent;
import co.gargoyle.rocnation.fragment.MerchandiseFragment;
import co.gargoyle.rocnation.fragment.MusicFragment;
import co.gargoyle.rocnation.fragment.PlanetFragment;
import co.gargoyle.rocnation.fragment.TicketsFragment;
import co.gargoyle.rocnation.fragment.VideoFragment;
import co.gargoyle.rocnation.list.NavAdapter;
import co.gargoyle.rocnation.service.MusicService;

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
public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mNavDrawerList;
	private ListView mVideoDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private FrameLayout mContentFrame;
	private RelativeLayout mVideoFrame;
	private RelativeLayout mPlayerFrame;

    private VideoView mVideoView;

	private Button mPlayButton;
	// private MusicService.ServiceBinder mPlaybackBinder;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;

	private boolean mIsBound = false;
	private MusicService mServ;

    @Inject com.squareup.otto.Bus bus;
    @Inject MusicFragment musicFragment;

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
        bus.register(this);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.nav_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavDrawerList = (ListView) findViewById(R.id.left_drawer);
        mVideoDrawerList = (ListView) findViewById(R.id.right_drawer);

        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);
        mVideoFrame   = (RelativeLayout) findViewById(R.id.video_frame);
        mPlayerFrame  = (RelativeLayout) findViewById(R.id.player_frame);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

        mVideoView   = (VideoView) findViewById(R.id.top_video);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        // Set video link (mp4 format )
        mVideoView.setMediaController(mediaController);

        mPlayButton = (Button) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(mOnPlayPressedListener);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mNavDrawerList.setAdapter(new NavAdapter(this));
        mNavDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mVideoDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));

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
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mNavDrawerList);
		//menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		menu.findItem(R.id.action_video).setVisible(!drawerOpen);
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
			mVideoFrame.setVisibility(View.VISIBLE);

			mPlayerFrame.setVisibility(View.GONE);
			mContentFrame.setVisibility(View.GONE);

			mVideoView.setVideoURI(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
			mVideoView.start();

            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
		} else {
			mVideoFrame.setVisibility(View.GONE);

			mPlayerFrame.setVisibility(View.VISIBLE);
			mContentFrame.setVisibility(View.VISIBLE);

            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

			// update the main content by replacing fragments
			Fragment fragment = getFragmentForPosition(position);
			swapFragment(fragment);
		}
	}

	private void updateSelectedItemAndCloseDrawer(int position) {
		mNavDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mNavDrawerList);
	}

	private Fragment getFragmentForPosition(int position) {
		Fragment fragment;

		switch (position) {
		case 0:
			//fragment = new MusicFragment();
			fragment = musicFragment;
			break;
		case 1:
			fragment = new VideoFragment();
			break;
		case 2:
			fragment = new TicketsFragment();
			break;
		case 3:
			fragment = new MerchandiseFragment();
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
		FragmentManager fragmentManager = getFragmentManager();
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
            //mServ.resumeMusic();
            // mServ.pauseMusic();
            // mServ.stopMusic();
		}
	};

    ////////////////////////////////////////////////////////////
	// Otto Subscriptions
	////////////////////////////////////////////////////////////

    @Subscribe
    public void musicTimeChanged(MusicTimeChangedEvent event) {
        Log.d("otto", "musicTimeChanged: " + String.valueOf(event.playbackTime));
    }

    @Subscribe
    public void onMusicPlaying(MusicPlayingEvent event) {
        Log.d("otto", "onMusicPlaying");
    }

    @Subscribe
    public void onMusicPaused(MusicPausedEvent event) {
        Log.d("otto", "onMusicPaused");

    }

	@Subscribe
    public void onMusicTrackChanged(MusicTrackChangeEvent event) {
        Log.d("otto", "musicTrackChanged: " + event.song);

		try {
			mServ.playSong(event.song);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
