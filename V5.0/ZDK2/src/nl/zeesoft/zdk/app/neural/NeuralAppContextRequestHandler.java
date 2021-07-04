package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.app.AppContextRequestHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexHtmlHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkConfigJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateHandler;

public class NeuralAppContextRequestHandler extends AppContextRequestHandler {
	public NeuralAppContextRequestHandler(NeuralApp app) {
		super(app);
	}
	
	@Override
	protected void initializeContextHandlers() {
		super.initializeContextHandlers();
		put(new IndexHtmlHandler((NeuralApp)app));
		put(new NetworkStateHandler((NeuralApp)app));
		put(new NetworkConfigJsonHandler((NeuralApp)app));
	}
}
