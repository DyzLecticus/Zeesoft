package nl.zeesoft.zdk.test.app;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.app.AppContextHandler;
import nl.zeesoft.zdk.app.AppContextRequestHandler;
import nl.zeesoft.zdk.app.AppStateHandler;
import nl.zeesoft.zdk.app.AppStateManager;
import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppConfig;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;

public class TestNeuralApp {
	protected static TestNeuralApp	self		= new TestNeuralApp();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		AppStateManager stateManager = new AppStateManager();
		assert stateManager.ifSetState(AppStateManager.STARTING);
		assert !stateManager.ifSetState(AppStateManager.STOPPED);
		assert stateManager.ifSetState(AppStateManager.STARTED);
		assert !stateManager.ifSetState(AppStateManager.STARTING);
		assert stateManager.ifSetState(AppStateManager.STOPPING);
		assert !stateManager.ifSetState(AppStateManager.STARTED);
		assert stateManager.ifSetState(AppStateManager.STOPPED);
		assert !stateManager.ifSetState(AppStateManager.STOPPING);
		
		NeuralAppConfig config = new NeuralAppConfig();
		NeuralApp app = new NeuralApp(config);
		app.setNetworkConfig(new NetworkConfig());
		assert app.getNetworkConfig() == null;
		assert app.getNetwork() == null;
		
		// Test context handlers
		HttpServerConfig serverConfig = config.loadHttpServerConfig(app);
		AppContextRequestHandler requestHandler = (AppContextRequestHandler) serverConfig.getRequestHandler();
		HttpRequest request = new HttpRequest(HttpRequest.GET,IndexHandler.PATH);
		HttpResponse response = new HttpResponse();
		requestHandler.handleRequest(request, response);
		assert response.getBody().length() == 0;
		assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;

		request = new HttpRequest(HttpRequest.GET,AppStateHandler.PATH);
		response = new HttpResponse();
		requestHandler.handleRequest(request, response);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().toString().equals(AppStateManager.STOPPED);

		AppContextHandler stateHandler = (AppContextHandler) requestHandler.get(AppStateHandler.PATH);
		request = new HttpRequest(HttpRequest.POST,AppStateHandler.PATH);
		response = new HttpResponse();
		stateHandler.handleRequest(request, response);
		assert response.getBody().length() == 0;
		assert response.code == HttpURLConnection.HTTP_NOT_IMPLEMENTED;
		
		NeuralAppContextHandler indexHandler = (NeuralAppContextHandler) requestHandler.get(IndexHandler.PATH);
		assert indexHandler.getServer() == null;
		assert indexHandler.getNetworkConfig() == null;
		assert indexHandler.getNetwork() == null;
		
		request = new HttpRequest(HttpRequest.GET,IndexHandler.PATH);
		response = new HttpResponse();
		indexHandler.handleRequest(request, response);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() > 0;

		request = new HttpRequest(HttpRequest.POST,IndexHandler.PATH);
		response = new HttpResponse();
		indexHandler.handleRequest(request, response);
		assert response.getBody().length() == 0;
		assert response.code == HttpURLConnection.HTTP_NOT_IMPLEMENTED;
		
		try {
			// Test app
			assert app.start();
			assert app.getState().equals(AppStateManager.STARTED);
			assert app.isStarted();
			assert app.getNetworkConfig() != null;
			assert app.getNetwork() != null;
			assert indexHandler.getServer().isOpen();
			NetworkConfig networkConfig = new NetworkConfig();
			indexHandler.setNetworkConfig(networkConfig);
			assert indexHandler.getNetworkConfig() == networkConfig;
			assert indexHandler.getNetwork().isInitialized();
			assert !app.start();
			
			request = new HttpRequest(HttpRequest.GET,AppStateHandler.PATH);
			response = new HttpResponse();
			requestHandler.handleRequest(request, response);
			assert response.code == HttpURLConnection.HTTP_OK;
			assert response.getBody().toString().equals(AppStateManager.STARTED);
			
			request = new HttpRequest(HttpRequest.GET,IndexHandler.PATH);
			response = new HttpResponse();
			requestHandler.handleRequest(request, response);
			assert response.code == HttpURLConnection.HTTP_OK;
			assert response.getBody().length() > 0;
			
			assert app.stop();
			assert !indexHandler.getServer().isOpen();
			assert !app.isStarted();
			assert !app.stop();
		} catch(AssertionError e) {
			e.printStackTrace();
			if (app.isStarted()) {
				app.stop();
			}
		}
	}
}
