package nl.zeesoft.zdk.app.neural;

import nl.zeesoft.zdk.app.App;
import nl.zeesoft.zdk.app.AppConfig;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;

public class NeuralAppConfig extends AppConfig {
	protected NetworkConfig		networkConfig	= null;
	protected Network			network			= new Network();

	@Override
	public synchronized void onAppStart() {
		super.onAppStart();
		networkConfig = loadNetworkConfig();
		network.initialize(networkConfig);
		if (!loadNetwork(network)) {
			network.reset();
		}
	}
	
	@Override
	public void onAppStop() {
		super.onAppStop();
		saveNetworkConfig(networkConfig);
		saveNetwork(network);
	}
	
	public NetworkConfig loadNetworkConfig() {
		NetworkConfigFactory factory = new NetworkConfigFactory();
		return factory.getSimpleConfig("input1", "input2");
	}
	
	public boolean loadNetwork(Network network) {
		// Override to implement; return true if loaded
		return false;
	}
	
	public void saveNetworkConfig(NetworkConfig config) {
		// Override to implement
	}
	
	public void saveNetwork(Network network) {
		// Override to implement
	}
	
	@Override
	public HttpServerConfig getNewHttpServerConfig(App app) {
		HttpServerConfig r = super.getNewHttpServerConfig(app);
		NeuralAppContextRequestHandler handler = new NeuralAppContextRequestHandler((NeuralApp)app);
		handler.initializeContextHandlers();
		r.setRequestHandler(handler);
		return r;
	}
}
