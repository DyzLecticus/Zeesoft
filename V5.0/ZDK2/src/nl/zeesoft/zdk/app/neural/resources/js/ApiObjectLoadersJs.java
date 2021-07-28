package nl.zeesoft.zdk.app.neural.resources.js;

import nl.zeesoft.zdk.app.AppStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkConfigJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOAccuracyJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOStatsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettingsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStatsJsonHandler;
import nl.zeesoft.zdk.app.resource.Resource;

public class ApiObjectLoadersJs extends Resource {
	@Override
	protected void render(StringBuilder r) {
		renderObjectLoaders(r);
		renderLoadApiObjects(r);
	}

	protected void renderObjectLoaders(StringBuilder r) {
		append(r, renderLoader("appState", AppStateTextHandler.PATH));
		append(r, renderLoader("networkState", NetworkStateTextHandler.PATH));
		append(r, renderLoader("networkConfig", NetworkConfigJsonHandler.PATH));
		append(r, renderLoader("networkSettings", NetworkSettingsJsonHandler.PATH));
		append(r, renderLoader("networkStats", NetworkStatsJsonHandler.PATH));
		append(r, "networkStatsLoader.autoRefreshMs = 30000;");
		append(r, renderLoader("networkIOStats", NetworkIOStatsJsonHandler.PATH));
		append(r, renderLoader("networkIOAccuracy", NetworkIOAccuracyJsonHandler.PATH));
	}
	
	protected void renderLoadApiObjects(StringBuilder r) {
		append(r, "var loadApiObjects = () => {");
		append(r, "    appStateLoader.execute();");
		append(r, "    networkStateLoader.execute();");
		append(r, "    networkConfigLoader.execute();");
		append(r, "    networkSettingsLoader.execute();");
		append(r, "    networkStatsLoader.execute();");
		append(r, "    networkIOStatsLoader.execute();");
		append(r, "    networkIOAccuracyLoader.execute();");
		append(r, "};");
	}

	protected StringBuilder renderLoader(String name, String path) {
		StringBuilder r = new StringBuilder();
		r.append("var ");
		r.append(name);
		r.append("Loader = new ObjectLoader(\"");
		r.append(path);
		r.append("\",\"");
		r.append(name);
		r.append("\");");
		return r;
	}
}
