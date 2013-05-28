package co.gargoyle.rocnation.inject;

import static android.content.Context.LOCATION_SERVICE;

import javax.inject.Named;
import javax.inject.Singleton;

import android.app.NotificationManager;
import android.content.Context;
import android.location.LocationManager;
import co.gargoyle.rocnation.RocApplication;
import co.gargoyle.rocnation.activity.MainActivity;
import co.gargoyle.rocnation.events.bus.AndroidBus;
import co.gargoyle.rocnation.fragment.MusicFragment;
import co.gargoyle.rocnation.fragment.VideoListFragment;
import co.gargoyle.rocnation.model.Video;
import co.gargoyle.rocnation.service.MusicService;

import com.activeandroid.DatabaseHelper;
import com.davidykay.energytracker.NotificationHelper;
import com.squareup.otto.Bus;

import dagger.Module;
import dagger.Provides;

//@Module(library = true)
@Module(
    injects = {
        MainActivity.class,
        MusicService.class,
        MusicFragment.class,
        VideoListFragment.class,
    },
    complete = false,
    library = true
)
public class RocModule {
  private final RocApplication application;
  private final DatabaseHelper databaseHelper;
  private final NotificationHelper notificationHelper;
  private final Bus bus = new AndroidBus();

  public RocModule(RocApplication application) {
	  this.application = application;
	  this.databaseHelper = new DatabaseHelper(application);
	  this.notificationHelper = new NotificationHelper(application, (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE));
  }

  /**
   * Allow the application context to be injected but require that it be annotated with
   * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
   */
  @Provides @Singleton @ForApplication Context provideApplicationContext() {
    return application;
  }

  @Provides @Singleton LocationManager provideLocationManager() {
    return (LocationManager) application.getSystemService(LOCATION_SERVICE);
  }

  @Provides @Singleton Bus provideBus() {
    return bus;
  }

	@Provides @Singleton NotificationManager provideNotificationManager() {
		return (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
  }

	@Provides @Singleton NotificationHelper provideNotificationHelper() {
    return notificationHelper;
  }

  @Provides @Singleton DatabaseHelper provideDatabaseHelper() {
    return databaseHelper;
  }

  @Provides @Named("music") VideoListFragment provideMusicVideoListFragment() {
    VideoListFragment fragment = new VideoListFragment();
    fragment.setMode(Video.MUSIC_VIDEO);
    application.inject(fragment);
    return fragment;
  }

  @Provides @Named("interview") VideoListFragment provideInterviewVideoListFragment() {
    VideoListFragment fragment = new VideoListFragment();
    fragment.setMode(Video.INTERVIEW);
    application.inject(fragment);
    return fragment;
  }

  @Provides @Named("making") VideoListFragment provideMakingOfVideoListFragment() {
    VideoListFragment fragment = new VideoListFragment();
    fragment.setMode(Video.BEHIND_THE_SCENES);
    application.inject(fragment);
    return fragment;
  }

}
