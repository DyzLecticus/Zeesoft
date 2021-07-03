package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.app.AppContextRequestHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexHandler;

public class NeuralAppContextRequestHandler extends AppContextRequestHandler {
	public NeuralAppContextRequestHandler(NeuralApp app) {
		super(app);
	}
	
	@Override
	protected void initializeContextHandlers() {
		super.initializeContextHandlers();
		put(new IndexHandler((NeuralApp)app));
	}
}
