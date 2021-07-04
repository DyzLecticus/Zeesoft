package nl.zeesoft.zdk.test.app;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.app.App;
import nl.zeesoft.zdk.app.AppConfig;
import nl.zeesoft.zdk.app.AppContextRequestHandler;
import nl.zeesoft.zdk.app.AppStateManager;
import nl.zeesoft.zdk.app.AppStateTextHandler;
import nl.zeesoft.zdk.app.neural.NetworkStateManager;
import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppConfig;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexHtmlHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkConfigJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateTextHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;

public class TestNeuralApp {
	protected static TestNeuralApp	self		= new TestNeuralApp();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		AppStateManager stateManager = new AppStateManager();
		assert stateManager.ifSetState(AppStateManager.STARTING);
		assert stateManager.ifSetState(AppStateManager.STOPPED);
		assert stateManager.ifSetState(AppStateManager.STARTING);
		assert !stateManager.ifSetState(AppStateManager.STOPPING);
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
		assert networkStateManager.ifSetState(NetworkStateManager.PROCESSING);
		assert !networkStateManager.ifSetState(NetworkStateManager.CREATED);
		assert networkStateManager.ifSetState(NetworkStateManager.READY);
		
		NeuralAppConfig config = new NeuralAppConfig();
		NeuralApp app = new NeuralApp(config);
		NetworkConfig networkConfig = new NetworkConfig();
		app.getNetworkManager().setConfig(networkConfig);
		assert app.getNetworkManager().getConfig() == networkConfig;
		assert app.getNetworkManager().getState().equals(NetworkStateManager.CREATED);
		assert !app.getNetworkManager().isReady();
		assert !app.getNetworkManager().loadNetwork();
		assert !app.getNetworkManager().resetNetwork();
		assert !app.getNetworkManager().saveNetwork();
		
		// Test context handlers
		HttpServerConfig serverConfig = config.loadHttpServerConfig(app);
		AppContextRequestHandler requestHandler = (AppContextRequestHandler) serverConfig.getRequestHandler();
		HttpRequest request = new HttpRequest(HttpRequest.GET,IndexHtmlHandler.PATH);
		HttpResponse response = new HttpResponse();
		requestHandler.handleRequest(request, response);
		assert response.getBody().length() == 0;
		assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;

		request = new HttpRequest(HttpRequest.GET,AppStateTextHandler.PATH);
		response = new HttpResponse();
		requestHandler.handleRequest(request, response);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().toString().equals(AppStateManager.STOPPED);
		
		request = new HttpRequest(HttpRequest.POST,AppStateTextHandler.PATH);
		response = new HttpResponse();
		requestHandler.handleRequest(request, response);
		assert response.getBody().length() == 0;
		assert response.code == HttpURLConnection.HTTP_BAD_METHOD;

		NeuralAppContextHandler indexHandler = (NeuralAppContextHandler) requestHandler.get(IndexHtmlHandler.PATH);
		assert indexHandler.getServer() == null;
		assert indexHandler.getNetworkManager() != null;
		assert indexHandler.getNetworkManager().getConfig() == networkConfig;
		
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
			
			//assert indexHandler.getNetworkManager().resetNetwork();
			
			request = new HttpRequest(HttpRequest.GET,AppStateTextHandler.PATH);
			response = requestHandler.handleRequest(request);
			assert response.code == HttpURLConnection.HTTP_OK;
			assert response.getBody().toString().equals(AppStateManager.STARTED);
			
			request = new HttpRequest(HttpRequest.GET,IndexHtmlHandler.PATH);
			response = requestHandler.handleRequest(request);
			assert response.code == HttpURLConnection.HTTP_OK;
			assert response.getBody().length() > 0;

			request = new HttpRequest(HttpRequest.GET,NetworkStateTextHandler.PATH);
			response = requestHandler.handleRequest(request);
			assert response.code == HttpURLConnection.HTTP_OK;
			assert response.getBody().toString().equals(NetworkStateManager.READY);

			request = new HttpRequest(HttpRequest.GET,NetworkConfigJsonHandler.PATH);
			response = requestHandler.handleRequest(request);
			assert response.code == HttpURLConnection.HTTP_OK;
			assert response.getBody().length() > 0;

			request = new HttpRequest(HttpRequest.POST,NetworkConfigJsonHandler.PATH);
			request.setBody(new Json());
			response = requestHandler.handleRequest(request);
			assert response.code == HttpURLConnection.HTTP_BAD_REQUEST;
			assert response.getBody().toString().equals("Failed to parse nl.zeesoft.zdk.neural.network.config.NetworkConfig from JSON");

			request = new HttpRequest(HttpRequest.POST,NetworkConfigJsonHandler.PATH);
			request.setBody(JsonConstructor.fromObject(networkConfig));
			response = requestHandler.handleRequest(request);
			assert response.code == HttpURLConnection.HTTP_BAD_REQUEST;
			assert response.getBody().toString().equals(
				"A network must have at least one input\n" + 
				"A network must have at least one processor"
			);

			networkConfig.addInput("Input");
			networkConfig.addScalarEncoder("Encoder");
			networkConfig.addLink("Input", "Encoder");
			
			request = new HttpRequest(HttpRequest.POST,NetworkConfigJsonHandler.PATH);
			request.setBody(JsonConstructor.fromObject(networkConfig));
			response = requestHandler.handleRequest(request);
			assert response.code == HttpURLConnection.HTTP_OK;
			assert response.getBody().length() == 0;

			request = new HttpRequest(HttpRequest.POST,NetworkIOJsonHandler.PATH);
			request.setBody(new Json());
			response = requestHandler.handleRequest(request);
			assert response.code == HttpURLConnection.HTTP_BAD_REQUEST;
			assert response.getBody().toString().equals("Failed to parse nl.zeesoft.zdk.neural.network.NetworkIO from JSON");


			NetworkIO io = new NetworkIO();
			io.addInput("Input", 1);
			request = new HttpRequest(HttpRequest.POST,NetworkIOJsonHandler.PATH);
			request.setBody(JsonConstructor.fromObject(io));
			response = requestHandler.handleRequest(request);
			assert response.code == HttpURLConnection.HTTP_OK;
			assert response.getBody().length() > 0;
			Json json = new Json(response.getBody());
			io = (NetworkIO) ObjectConstructor.fromJson(json);
			assert io.getProcessorIO("Encoder").outputs.size() == 1;
			assert io.getProcessorIO("Encoder").outputs.get(0).onBits.size() == 16;

			AppConfig testConfig = new AppConfig(); 
			App testApp = new App(testConfig);
			assert !testApp.start();
			assert testApp.getState().equals(AppStateManager.STOPPED);
			
			assert app.stop();
			assert !indexHandler.getServer().isOpen();
			assert !app.isStarted();
			assert !app.stop();
			
			Util.sleep(100);
			Logger.debug(self, "Test success!");
		} catch(AssertionError e) {
			e.printStackTrace();
			if (app.isStarted()) {
				app.stop();
			}
		}
	}
}
