package co.gargoyle.rocnation;

import java.util.Arrays;
import java.util.List;

import android.app.Application;
import co.gargoyle.rocnation.inject.RocModule;
import dagger.ObjectGraph;

public class RocApplication extends Application {
	private ObjectGraph graph;

	@Override public void onCreate() {
		super.onCreate();

		graph = ObjectGraph.create(getModules().toArray());
	}

    protected List<Object> getModules() {
        return Arrays.<Object>asList(
                new RocModule(this)
                );
    }

	public void inject(Object object) {
		graph.inject(object);
	}

    public ObjectGraph getApplicationGraph() {
        return graph;
    }
}
