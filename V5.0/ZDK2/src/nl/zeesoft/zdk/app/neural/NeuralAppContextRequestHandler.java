package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.app.AppContextRequestHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexHtmlHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkConfigJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettingsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStatsJsonHandler;

public class NeuralAppContextRequestHandler extends AppContextRequestHandler {
	public NeuralAppContextRequestHandler(NeuralApp app) {
		super(app);
	}
	
	@Override
	protected void initializeContextHandlers() {
		super.initializeContextHandlers();
		put(new IndexHtmlHandler((NeuralApp)app));
		put(new IndexJsHandler((NeuralApp)app));
		
		put(new NetworkStateTextHandler((NeuralApp)app));
		put(new NetworkConfigJsonHandler((NeuralApp)app));
		put(new NetworkSettingsJsonHandler((NeuralApp)app));
		put(new NetworkIOJsonHandler((NeuralApp)app));
		put(new NetworkStatsJsonHandler((NeuralApp)app));
	}
}
