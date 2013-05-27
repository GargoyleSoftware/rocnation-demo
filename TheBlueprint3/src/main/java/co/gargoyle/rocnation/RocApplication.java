package co.gargoyle.rocnation;

import java.util.Arrays;
import java.util.List;

import co.gargoyle.rocnation.inject.RocModule;

import com.activeandroid.ActiveAndroid;

import dagger.ObjectGraph;

public class RocApplication extends com.activeandroid.app.Application {
	private ObjectGraph graph;

	@Override public void onCreate() {
		super.onCreate();

		//		String customFontPath = "DINOT-Cond.otf";
		//		CustomFont.setFontsToUse(customFontPath, customFontPath, customFontPath, customFontPath);

		ActiveAndroid.initialize(this);
		graph = ObjectGraph.create(getModules().toArray());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
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
