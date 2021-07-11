package nl.zeesoft.zdk.test.app;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkConfigJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettings;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettingsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStatsJsonHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpRequestHandler;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.http.HttpServerConfig;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;
import nl.zeesoft.zdk.neural.network.NetworkIO;
import nl.zeesoft.zdk.neural.network.config.NetworkConfig;
import nl.zeesoft.zdk.str.StrUtil;

public class TestNeuralApp {
	private static TestNeuralApp					self			= new TestNeuralApp();
	
	private static CopyOnWriteArrayList<Integer>	responseCodes	= new CopyOnWriteArrayList<Integer>(); 
	
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
		app.getNetworkManager().setWorkers(2);
		app.getNetworkManager().setInitTimeoutMs(10001);
		app.getNetworkManager().setResetTimeoutMs(10002);
		assert app.getNetworkManager().getWorkers() == 2;
		assert app.getNetworkManager().getInitTimeoutMs() == 10001;
		assert app.getNetworkManager().getResetTimeoutMs() == 10002;
		assert !app.getNetworkManager().isReady();
		assert !app.getNetworkManager().loadNetwork();
		assert !app.getNetworkManager().resetNetwork();
		assert !app.getNetworkManager().saveNetwork();
		assert !app.getNetworkManager().setProcessorLearning(null);
		assert app.getNetworkManager().getProcessorLearning() == null;
		assert app.getNetworkManager().getCellStats() == null;
		
		// Test context handlers
		HttpServerConfig serverConfig = config.loadHttpServerConfig(app);
		AppContextRequestHandler requestHandler = (AppContextRequestHandler) serverConfig.getRequestHandler();
		HttpRequest request = new HttpRequest(HttpRequest.GET,IndexHtmlHandler.PATH);
		HttpResponse response = new HttpResponse();
		requestHandler.handleRequest(request, response);
		assert response.getBody().length() == 0;
		assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;
		String startsWith = "- /app/state.txt (HEAD, GET)\n- / (HEAD, GET)";
		assert StrUtil.startsWith(requestHandler.getPathHandlersStringBuilder(), startsWith);

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
		
		// TODO: Remove try / catch
		try {
			// Test app
			assert app.start();
			assert app.getState().equals(AppStateManager.STARTED);
			assert app.isStarted();
			assert app.getNetworkManager() != null;
			assert app.getNetworkManager().isReady();
			assert indexHandler.getServer().isOpen();
			assert !app.start();

			testRequests(requestHandler);

			NetworkSettings settings = new NetworkSettings();
			assert app.getNetworkManager().getWorkers() == 3;
			assert app.getNetworkManager().getInitTimeoutMs() == 10101;
			assert app.getNetworkManager().getResetTimeoutMs() == 10102;
			settings.configure(app.getNetworkManager());
			assert app.getNetworkManager().getWorkers() == 3;
			assert app.getNetworkManager().getInitTimeoutMs() == 10101;
			assert app.getNetworkManager().getResetTimeoutMs() == 10102;

			AppConfig testConfig = new AppConfig(); 
			App testApp = new App(testConfig);
			assert !testApp.start();
			assert testApp.getState().equals(AppStateManager.STOPPED);
			
			assert app.stop();
			assert !indexHandler.getServer().isOpen();
			assert !app.isStarted();
			assert !app.stop();
			
			networkConfig = getSimpleNetworkConfig();
			MockNeuralApp mockApp = new MockNeuralApp(config);
			NetworkConfigJsonHandler configJsonHandler = new NetworkConfigJsonHandler(mockApp);
			request = new HttpRequest(HttpRequest.POST,NetworkConfigJsonHandler.PATH);
			request.setBody(JsonConstructor.fromObject(networkConfig));
			response = new HttpResponse();
			configJsonHandler.handleRequest(request, response);
			assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;
			assert !((MockNetworkManager)mockApp.getNetworkManager()).initializeNetwork();
			
			Util.sleep(100);
			Logger.debug(self, "Test success!");
		} catch(AssertionError e) {
			e.printStackTrace();
			if (app.isStarted()) {
				app.stop();
			}
		}
	}
	
	private static void testRequests(HttpRequestHandler requestHandler) {
		HttpRequest request = null;
		HttpResponse response = null;
		
		NetworkConfig networkConfig = new NetworkConfig();
		
		// App state
		request = new HttpRequest(HttpRequest.HEAD,AppStateTextHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() == 0;

		request = new HttpRequest(HttpRequest.GET,AppStateTextHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().toString().equals(AppStateManager.STARTED);
		
		// App index html
		request = new HttpRequest(HttpRequest.HEAD,IndexHtmlHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() == 0;
		
		request = new HttpRequest(HttpRequest.GET,IndexHtmlHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() > 0;
		
		// App index js
		request = new HttpRequest(HttpRequest.HEAD,IndexJsHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() == 0;
		
		request = new HttpRequest(HttpRequest.GET,IndexJsHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() > 0;

		// Network state
		request = new HttpRequest(HttpRequest.HEAD,NetworkStateTextHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() == 0;
		
		request = new HttpRequest(HttpRequest.GET,NetworkStateTextHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().toString().equals(NetworkStateManager.READY);

		// Network config
		request = new HttpRequest(HttpRequest.HEAD,NetworkConfigJsonHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() == 0;
		
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

		networkConfig = getSimpleNetworkConfig();

		HttpRequest request2 = new HttpRequest(HttpRequest.POST,NetworkIOJsonHandler.PATH);
		request2.setBody(JsonConstructor.fromObject(new NetworkIO()));
		HttpRequest request3 = new HttpRequest(HttpRequest.GET,NetworkSettingsJsonHandler.PATH);
		HttpRequest request4 = new HttpRequest(HttpRequest.POST,NetworkSettingsJsonHandler.PATH);
		NetworkSettings settings = new NetworkSettings();
		settings.processorLearning.put("Pooler", false);
		request4.setBody(JsonConstructor.fromObject(settings));

		request = new HttpRequest(HttpRequest.POST,NetworkConfigJsonHandler.PATH);
		request.setBody(JsonConstructor.fromObject(networkConfig));
		List<Thread> tests = new ArrayList<Thread>();
		tests.add(getRequestAssertUnavailableThread(requestHandler, request));
		tests.add(getRequestAssertUnavailableThread(requestHandler, request2));
		tests.add(getRequestAssertUnavailableThread(requestHandler, request3));
		tests.add(getRequestAssertUnavailableThread(requestHandler, request4));
		for (Thread test: tests) {
			test.start();
		}
		for (int i = 0; i < 10; i++) {
			response = requestHandler.handleRequest(request);
		}
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() == 0;
		assert responseCodes.size() == 4;
		for(Integer code: responseCodes) {
			assert code == HttpURLConnection.HTTP_UNAVAILABLE;
		}
		
		request = new HttpRequest(HttpRequest.POST,NetworkIOJsonHandler.PATH);
		request.setBody(new Json());
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_BAD_REQUEST;
		assert response.getBody().toString().equals("Failed to parse nl.zeesoft.zdk.neural.network.NetworkIO from JSON");

		// Network settings
		request = new HttpRequest(HttpRequest.HEAD,NetworkSettingsJsonHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() == 0;
		
		request = new HttpRequest(HttpRequest.GET,NetworkSettingsJsonHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() > 0;

		request = new HttpRequest(HttpRequest.POST,NetworkSettingsJsonHandler.PATH);
		request.setBody(new Json());
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_BAD_REQUEST;
		assert response.getBody().toString().equals("Failed to parse nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettings from JSON");

		settings = new NetworkSettings();
		settings.workers = 3;
		settings.initTimeoutMs = 10101;
		settings.resetTimeoutMs = 10102;
		settings.processorLearning.put("Pooler", false);
		
		request = new HttpRequest(HttpRequest.POST,NetworkSettingsJsonHandler.PATH);
		request.setBody(JsonConstructor.fromObject(settings));
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() == 0;

		request = new HttpRequest(HttpRequest.GET,NetworkSettingsJsonHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() > 0;
		
		Json json = new Json(response.getBody());
		assert (int)json.root.get("workers").value == 3;
		assert (int)json.root.get("initTimeoutMs").value == 10101;
		assert (int)json.root.get("resetTimeoutMs").value == 10102;
		assert (boolean)json.root.get("processorLearning").get("keyValues").get(0).get("value").get("value").value == false;
		
		// Network IO
		NetworkIO io = new NetworkIO();
		io.addInput("Input", 1);
		request = new HttpRequest(HttpRequest.POST,NetworkIOJsonHandler.PATH);
		request.setBody(JsonConstructor.fromObject(io));
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() > 0;
		json = new Json(response.getBody());
		io = (NetworkIO) ObjectConstructor.fromJson(json);
		assert io.getProcessorIO("Encoder").outputs.size() == 1;
		assert io.getProcessorIO("Encoder").outputs.get(0).onBits.size() == 16;

		// Network stats
		request = new HttpRequest(HttpRequest.HEAD,NetworkStatsJsonHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() == 0;
		
		request = new HttpRequest(HttpRequest.GET,NetworkStatsJsonHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() > 0;

		json = new Json(response.getBody());
		assert (int)json.root.get("proximalStats").get("activeSynapses").value >= 100000;
		assert (int)json.root.get("proximalStats").get("segments").value >= 1000;
		assert (int)json.root.get("proximalStats").get("synapses").value >= 100000;
	}
	
	private static NetworkConfig getSimpleNetworkConfig() {
		NetworkConfig r = new NetworkConfig();
		r.addInput("Input");
		r.addScalarEncoder("Encoder");
		r.addLink("Input", "Encoder");
		r.addSpatialPooler("Pooler");
		r.addLink("Encoder", "Pooler");
		return r;
	}
	
	private static Thread getRequestAssertUnavailableThread(HttpRequestHandler handler, HttpRequest request) {
		return new Thread() {
			@Override
			public void run() {
				Util.sleep(50);
				HttpResponse response = handler.handleRequest(request);
				responseCodes.add(response.code);
			}
		};
	}
}
