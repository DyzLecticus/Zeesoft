package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.app.AppContextRequestHandler;
import nl.zeesoft.zdk.app.neural.handlers.DemoTrainerHtmlHandler;
import nl.zeesoft.zdk.app.neural.handlers.DemoTrainerJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.FaviconIcoHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexCssHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexHtmlHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkConfigJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOAccuracyJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOStatsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettingsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStatsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.RegularDateTimeValuesCsvHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.SdrPngHandler;

public class NeuralAppContextRequestHandler extends AppContextRequestHandler {
	public NeuralAppContextRequestHandler(NeuralApp app) {
		super(app);
	}
	
	@Override
	protected void initializeContextHandlers() {
		super.initializeContextHandlers();
		put(new IndexHtmlHandler((NeuralApp)app));
		put(new FaviconIcoHandler((NeuralApp)app));
		put(new IndexJsHandler((NeuralApp)app));
		put(new IndexCssHandler((NeuralApp)app));
		put(new DemoTrainerHtmlHandler((NeuralApp)app));
		put(new DemoTrainerJsHandler((NeuralApp)app));
		initializeNetworkContextHandlers();
	}
	
	protected void initializeNetworkContextHandlers() {
		put(new NetworkStateTextHandler((NeuralApp)app));
		put(new NetworkConfigJsonHandler((NeuralApp)app));
		put(new NetworkSettingsJsonHandler((NeuralApp)app));
		
		put(new NetworkStatsJsonHandler((NeuralApp)app));
		put(new NetworkIOStatsJsonHandler((NeuralApp)app));
		put(new NetworkIOAccuracyJsonHandler((NeuralApp)app));
		
		put(new NetworkIOJsonHandler((NeuralApp)app));
		put(new SdrPngHandler((NeuralApp)app));
		put(new RegularDateTimeValuesCsvHandler((NeuralApp)app));
	}
}
