package co.gargoyle.rocnation.inject;

import static android.content.Context.LOCATION_SERVICE;

import javax.inject.Singleton;

import android.content.Context;
import android.location.LocationManager;
import co.gargoyle.rocnation.RocApplication;
import co.gargoyle.rocnation.activity.MainActivity;
import co.gargoyle.rocnation.service.MusicService;

import com.squareup.otto.Bus;

import dagger.Module;
import dagger.Provides;

//@Module(library = true)
@Module(
    injects = {
        MainActivity.class,
        MusicService.class,
    },
    complete = false,
    library = true
)
public class RocModule {
  private final RocApplication application;
  private final Bus bus = new Bus();

  public RocModule(RocApplication application) {
    this.application = application;
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
}
