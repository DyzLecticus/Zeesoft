package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.app.App;
import nl.zeesoft.zdk.app.AppConfig;
import nl.zeesoft.zdk.http.HttpServerConfig;

public class NeuralAppConfig extends AppConfig {
	protected NetworkManager	networkManager	= null;

	public NeuralAppConfig() {
		networkManager = getNewNetworkManager();
	}
	
	@Override
	public void onAppStart() {
		super.onAppStart();
		networkManager.onAppStart();
	}
	
	@Override
	public void onAppStop() {
		super.onAppStop();
		networkManager.onAppStop();
	}
	
	@Override
	public HttpServerConfig getNewHttpServerConfig(App app) {
		HttpServerConfig r = super.getNewHttpServerConfig(app);
		NeuralAppContextRequestHandler handler = new NeuralAppContextRequestHandler((NeuralApp)app);
		handler.initializeContextHandlers();
		r.setRequestHandler(handler);
		return r;
	}
	
	public NetworkManager getNetworkManager() {
		return networkManager;
	}
	
	protected NetworkManager getNewNetworkManager() {
		return new NetworkManager();
	}
}
