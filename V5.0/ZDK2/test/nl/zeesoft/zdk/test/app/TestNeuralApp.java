package nl.zeesoft.zdk.test.app;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.app.AppContextRequestHandler;
import nl.zeesoft.zdk.app.AppStateHandler;
import nl.zeesoft.zdk.app.AppStateManager;
import nl.zeesoft.zdk.app.neural.NetworkStateManager;
import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppConfig;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexHtmlHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateHandler;
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
		
		NetworkStateManager networkStateManager = new NetworkStateManager();
		assert !networkStateManager.ifSetState(NetworkStateManager.READY);
		assert networkStateManager.ifSetState(NetworkStateManager.INITIALIZING);
		assert !networkStateManager.ifSetState(NetworkStateManager.CREATED);
		assert networkStateManager.ifSetState(NetworkStateManager.LOADING);
		assert !networkStateManager.ifSetState(NetworkStateManager.INITIALIZING);
		assert networkStateManager.ifSetState(NetworkStateManager.READY);
		assert !networkStateManager.ifSetState(NetworkStateManager.LOADING);
		assert networkStateManager.ifSetState(NetworkStateManager.SAVING);
		assert !networkStateManager.ifSetState(NetworkStateManager.CREATED);
		assert networkStateManager.ifSetState(NetworkStateManager.READY);
		assert networkStateManager.ifSetState(NetworkStateManager.RESETTING);
		assert !networkStateManager.ifSetState(NetworkStateManager.CREATED);
		assert networkStateManager.ifSetState(NetworkStateManager.READY);
		assert networkStateManager.ifSetState(NetworkStateManager.TRAINING);
		assert !networkStateManager.ifSetState(NetworkStateManager.CREATED);
		assert networkStateManager.ifSetState(NetworkStateManager.READY);
		
		NeuralAppConfig config = new NeuralAppConfig();
		NeuralApp app = new NeuralApp(config);
		NetworkConfig networkConfig = new NetworkConfig();
		app.getNetworkManager().setNetworkConfig(networkConfig);
		assert app.getNetworkManager().getNetworkConfig() == networkConfig;
		assert app.getNetworkManager().getState().equals(NetworkStateManager.CREATED);
		assert !app.getNetworkManager().isReady();
		
		// Test context handlers
		HttpServerConfig serverConfig = config.loadHttpServerConfig(app);
		AppContextRequestHandler requestHandler = (AppContextRequestHandler) serverConfig.getRequestHandler();
		HttpRequest request = new HttpRequest(HttpRequest.GET,IndexHtmlHandler.PATH);
		HttpResponse response = new HttpResponse();
		requestHandler.handleRequest(request, response);
		assert response.getBody().length() == 0;
		assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;

		request = new HttpRequest(HttpRequest.GET,AppStateHandler.PATH);
		response = new HttpResponse();
		requestHandler.handleRequest(request, response);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().toString().equals(AppStateManager.STOPPED);
		
		request = new HttpRequest(HttpRequest.POST,AppStateHandler.PATH);
		response = new HttpResponse();
		requestHandler.handleRequest(request, response);
		assert response.getBody().length() == 0;
		assert response.code == HttpURLConnection.HTTP_BAD_METHOD;

		NeuralAppContextHandler indexHandler = (NeuralAppContextHandler) requestHandler.get(IndexHtmlHandler.PATH);
		assert indexHandler.getServer() == null;
		assert indexHandler.getNetworkManager() != null;
		assert indexHandler.getNetworkManager().getNetworkConfig() == networkConfig;
		
		request = new HttpRequest(HttpRequest.GET,IndexHtmlHandler.PATH);
		response = new HttpResponse();
		indexHandler.handleRequest(request, response);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() > 0;
		
		try {
			// Test app
			assert app.start();
			assert app.getState().equals(AppStateManager.STARTED);
			assert app.isStarted();
			assert app.getNetworkManager() != null;
			assert app.getNetworkManager().isReady();
			assert indexHandler.getServer().isOpen();
			assert !app.start();
			
			assert indexHandler.getNetworkManager().resetNetwork();
			
			request = new HttpRequest(HttpRequest.GET,AppStateHandler.PATH);
			response = new HttpResponse();
			requestHandler.handleRequest(request, response);
			assert response.code == HttpURLConnection.HTTP_OK;
			assert response.getBody().toString().equals(AppStateManager.STARTED);
			
			request = new HttpRequest(HttpRequest.GET,IndexHtmlHandler.PATH);
			response = new HttpResponse();
			requestHandler.handleRequest(request, response);
			assert response.code == HttpURLConnection.HTTP_OK;
			assert response.getBody().length() > 0;

			request = new HttpRequest(HttpRequest.GET,NetworkStateHandler.PATH);
			response = new HttpResponse();
			requestHandler.handleRequest(request, response);
			assert response.code == HttpURLConnection.HTTP_OK;
			assert response.getBody().toString().equals(NetworkStateManager.READY);

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
