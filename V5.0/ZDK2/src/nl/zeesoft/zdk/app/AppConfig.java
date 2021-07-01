package nl.zeesoft.zdk.app;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.app.handlers.IndexHandler;
import nl.zeesoft.zdk.http.HttpContextRequestHandler;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.neural.network.Network;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfigFactory;

public class AppConfig {
	public HttpServerConfig loadHttpServerConfig(App app) {
		return getNewHttpServerConfig(app);
	}
	
	public NetworkConfig loadNetworkConfig() {
		NetworkConfigFactory factory = new NetworkConfigFactory();
		return factory.getSimpleConfig("input1", "input2");
	}
	
	public boolean loadNetwork(Network network) {
		// Override to implement
		return false;
	}
	
	public void saveNetworkConfig(NetworkConfig config) {
		// Override to implement
	}
	
	public void saveNetwork(Network network) {
		// Override to implement
	}
	
	public HttpServerConfig getNewHttpServerConfig(App app) {
		HttpServerConfig r = new HttpServerConfig();
		r.setPort(1234);
		r.setDebugLogHeaders(Logger.isLoggerDebug());
		HttpContextRequestHandler handler = new HttpContextRequestHandler();
		handler.put(new IndexHandler(app));
		r.setRequestHandler(handler);
		return r;
	}
}
