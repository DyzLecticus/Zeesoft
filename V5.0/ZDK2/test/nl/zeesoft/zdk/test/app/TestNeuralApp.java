package nl.zeesoft.zdk.test.app;

import java.awt.event.ActionEvent;
import java.net.HttpURLConnection;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JFrame;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.app.App;
import nl.zeesoft.zdk.app.AppActionHandler;
import nl.zeesoft.zdk.app.AppConfig;
import nl.zeesoft.zdk.app.AppContextRequestHandler;
import nl.zeesoft.zdk.app.AppStateManager;
import nl.zeesoft.zdk.app.AppStateTextHandler;
import nl.zeesoft.zdk.app.AppTrayIcon;
import nl.zeesoft.zdk.app.neural.NetworkManager;
import nl.zeesoft.zdk.app.neural.NetworkStateManager;
import nl.zeesoft.zdk.app.neural.NeuralApp;
import nl.zeesoft.zdk.app.neural.NeuralAppConfig;
import nl.zeesoft.zdk.app.neural.NeuralAppContextHandler;
import nl.zeesoft.zdk.app.neural.handlers.DemoTrainerHtmlHandler;
import nl.zeesoft.zdk.app.neural.handlers.DemoTrainerJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.FaviconIcoHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexCssHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexHtmlHandler;
import nl.zeesoft.zdk.app.neural.handlers.IndexJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.ManagerHtmlHandler;
import nl.zeesoft.zdk.app.neural.handlers.ManagerJsHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkConfigJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOAccuracyJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkIOStatsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettings;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettingsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStateTextHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.NetworkStatsJsonHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.RegularDateTimeValuesCsvHandler;
import nl.zeesoft.zdk.app.neural.handlers.api.SdrPngHandler;
import nl.zeesoft.zdk.app.resource.HtmlResource;
import nl.zeesoft.zdk.http.HttpHeader;
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
	private static TestNeuralApp	self	= new TestNeuralApp();
	
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
		
		HtmlResource html = new HtmlResource();
		assert html.render().length() == 207;
		assert HtmlResource.renderLinkListItem("A","B").toString().equals("<li><a href=\"A\">B</a></li>");
		
		NeuralAppConfig config = new NeuralAppConfig() {
			@Override
			protected NetworkManager getNewNetworkManager() {
				super.getNewNetworkManager();
				return new MockNetworkManager2();
			}
			
		};
		config.port = 9876;
		config.destroyMessageMs = 0;
						
		NeuralApp app = new NeuralApp(config);
		assert app.getName().equals("Zeesoft NeuralServer");
		assert app.getSelfUrl().equals("http://127.0.0.1:9876/");
		assert app.getTrayIcon() == null;
		assert !JFrame.isDefaultLookAndFeelDecorated();
		AppTrayIcon.setSystemLookAndFeel(true);
		assert JFrame.isDefaultLookAndFeelDecorated();
		
		AppActionHandler actionHandler = config.getNewAppActionHandler();
		actionHandler.initialize(app);
		
		actionHandler.actionPerformed(new ActionEvent(self, 0, AppActionHandler.QUIT));
		actionHandler.confirmQuit = false;
		actionHandler.actionPerformed(new ActionEvent(self, 0, AppActionHandler.QUIT));

		actionHandler.actionPerformed(new ActionEvent(self, 0, AppActionHandler.OPEN));
		actionHandler.openDesktopBrowser = false;
		actionHandler.actionPerformed(new ActionEvent(self, 0, AppActionHandler.OPEN));
		actionHandler.openMacBrowser = false;
		actionHandler.actionPerformed(new ActionEvent(self, 0, AppActionHandler.OPEN));

		actionHandler.actionPerformed(new ActionEvent(self, 0, ""));

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
		app.getNetworkManager().setWorkers(-1);
		app.getNetworkManager().setInitTimeoutMs(999);
		app.getNetworkManager().setResetTimeoutMs(999);
		assert app.getNetworkManager().getWorkers() == 2;
		assert app.getNetworkManager().getInitTimeoutMs() == 10001;
		assert app.getNetworkManager().getResetTimeoutMs() == 10002;
		assert !app.getNetworkManager().isReady();
		assert !app.getNetworkManager().loadNetwork();
		assert !app.getNetworkManager().resetNetwork();
		assert !app.getNetworkManager().saveNetwork();
		assert !app.getNetworkManager().setProcessorLearningAndWorkers(null, null);
		assert !app.getNetworkManager().setProcessorLearningAndWorkers(null);
		assert app.getNetworkManager().getCellStats() == null;
		
		SortedMap<String,Integer> processorWorkers = new TreeMap<String,Integer>();
		assert processorWorkers.size() == 0;
		processorWorkers.put("qwer", -1);
		app.getNetworkManager().setProcessorWorkers(processorWorkers);
		assert app.getNetworkManager().getProcessorWorkers().size() == 0;
		processorWorkers.put("qwer", 4);
		app.getNetworkManager().setProcessorWorkers(processorWorkers);
		assert app.getNetworkManager().getProcessorWorkers().size() == 0;
		app.getNetworkManager().getProcessorWorkers().put("qwer", 0);
		processorWorkers.put("qwer", 4);
		app.getNetworkManager().setProcessorWorkers(processorWorkers);
		assert app.getNetworkManager().getProcessorWorkers().get("qwer") == 4;
		processorWorkers.put("qwer", -1);
		app.getNetworkManager().setProcessorWorkers(processorWorkers);
		assert app.getNetworkManager().getProcessorWorkers().get("qwer") == 4;
		app.getNetworkManager().getProcessorWorkers().clear();
		
		assert app.getNetworkRecorder().getNetworkIO().size() == 0;
		NetworkIO io = new NetworkIO();
		app.getNetworkRecorder().add(io);
		assert app.getNetworkRecorder().getNetworkIO().size() == 1;
		assert app.getNetworkRecorder().getAverageStats().totalNs == 0;
		assert app.getNetworkRecorder().getAccuracy(100).getAverage().accuracy == 0F;
		app.getNetworkRecorder().reset();
		assert app.getNetworkRecorder().getNetworkIO().size() == 0;
		
		// Test context handlers
		HttpServerConfig serverConfig = config.loadHttpServerConfig(app);
		assert serverConfig.getPort() == 9876;
		AppContextRequestHandler requestHandler = (AppContextRequestHandler) serverConfig.getRequestHandler();
		HttpRequest request = new HttpRequest(HttpRequest.GET,IndexHtmlHandler.PATH);
		HttpResponse response = new HttpResponse();
		requestHandler.handleRequest(request, response);
		assert response.getBody().length() == 0;
		assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;
		String startsWith = "- /app/state.txt (HEAD, GET)\n- /index.html (HEAD, GET)";
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
		assert StrUtil.indexOf(response.getBody(), "href=\"" + DemoTrainerHtmlHandler.PATH + "\"", 0) >= 0;

		// Test app
		assert app.start();
		assert app.getState().equals(AppStateManager.STARTED);
		assert app.isStarted();
		assert app.getTrayIcon() != null;
		assert app.getTrayIcon().getPopupMenu() != null;		
		assert app.getNetworkManager() != null;
		assert app.getNetworkManager().isReady();
		assert indexHandler.getServer().isOpen();
		assert !app.start();

		NetworkSettings settings = new NetworkSettings();
		assert app.getNetworkManager().setProcessorLearningAndWorkers(settings);
		assert settings.processorWorkers.size() == 2;

		testRequests(requestHandler, (MockNetworkManager2) app.getNetworkManager());

		settings = new NetworkSettings();
		assert app.getNetworkManager().setProcessorLearningAndWorkers(settings);
		assert settings.processorWorkers.size() == 1;

		settings = new NetworkSettings();
		assert app.getNetworkManager().getWorkers() == 3;
		assert app.getNetworkManager().getInitTimeoutMs() == 10101;
		assert app.getNetworkManager().getResetTimeoutMs() == 10102;
		settings.configure(app.getNetworkManager());
		assert app.getNetworkManager().getWorkers() == 3;
		assert app.getNetworkManager().getInitTimeoutMs() == 10101;
		assert app.getNetworkManager().getResetTimeoutMs() == 10102;
		settings.processorWorkers.put("qwer", 1);
		assert settings.configure(app.getNetworkManager());

		AppConfig testConfig = new AppConfig(); 
		testConfig.port = 9876;
		testConfig.destroyMessageMs = 0;
		App testApp = new App(testConfig);
		assert !testApp.start();
		assert testApp.getState().equals(AppStateManager.STOPPED);
		
		app.getTrayIcon().destroy();
		assert app.getTrayIcon().getPopupMenu() == null;
		app.getTrayIcon().destroy();
		assert app.getTrayIcon().initialize(new AppActionHandler(), "Pizza", true) == false;

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
		
		config = new NeuralAppConfig();
		config.port = 80;
		app = new NeuralApp(config);
		serverConfig = config.getNewHttpServerConfig(app);
		indexHandler = (IndexHtmlHandler)((AppContextRequestHandler) serverConfig.getRequestHandler()).get(IndexHtmlHandler.PATH);
		request = new HttpRequest(HttpRequest.GET, IndexHtmlHandler.PATH);
		response = new HttpResponse();
		indexHandler.handleRequest(request, response);
		assert StrUtil.indexOf(response.getBody(), "href=\"" + DemoTrainerHtmlHandler.PATH + "\"", 0) == -1;
		
		Util.sleep(100);
		Logger.debug(self, "Test success!");
	}
	
	private static void testRequests(HttpRequestHandler requestHandler, MockNetworkManager2 mockNetworkManager) {
		HttpRequest request = null;
		HttpResponse response = null;
		
		NetworkConfig networkConfig = new NetworkConfig();
		
		// App state
		StringBuilder body = testHeadGetRequest(requestHandler, AppStateTextHandler.PATH, "text/plain");
		assert body.toString().equals(AppStateManager.STARTED);
		
		// App index html
		body = testHeadGetRequest(requestHandler, IndexHtmlHandler.PATH, "text/html");

		// App favicon ico
		body = testHeadGetRequest(requestHandler, FaviconIcoHandler.PATH, "image/x-icon");

		// App index js
		body = testHeadGetRequest(requestHandler, IndexJsHandler.PATH, "application/javascript");
		
		// App index css
		body = testHeadGetRequest(requestHandler, IndexCssHandler.PATH, "text/css");

		// App manager html
		body = testHeadGetRequest(requestHandler, ManagerHtmlHandler.PATH, "text/html");

		// App manager js
		body = testHeadGetRequest(requestHandler, ManagerJsHandler.PATH, "application/javascript");

		// App demo trainer html
		body = testHeadGetRequest(requestHandler, DemoTrainerHtmlHandler.PATH, "text/html");

		// App demo trainer js
		body = testHeadGetRequest(requestHandler, DemoTrainerJsHandler.PATH, "application/javascript");

		// Sdr png image
		body = testHeadGetRequest(requestHandler, SdrPngHandler.PATH + "?4,2", "image/png");
		assert body.length() == 92;
		request = new HttpRequest("GET",SdrPngHandler.PATH);
		request.query.append("sdr=4,2");
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		body = response.getBody();
		assert body.length() == 92;

		request = new HttpRequest("GET",SdrPngHandler.PATH);
		request.query.append("sdr=8100,0");
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		body = response.getBody();
		assert body.length() == 1878;

		request = new HttpRequest("GET",SdrPngHandler.PATH);
		request.query.append("sdr=8200,0");
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		body = response.getBody();
		assert body.length() == 859;

		request = new HttpRequest("GET",SdrPngHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_BAD_REQUEST;
		
		// Network state
		body = testHeadGetRequest(requestHandler, NetworkStateTextHandler.PATH, "text/plain");
		assert body.toString().equals(NetworkStateManager.READY);

		request = new HttpRequest(HttpRequest.POST,NetworkStateTextHandler.PATH);
		request.setBody(new StringBuilder("RESET"));
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		
		request = new HttpRequest(HttpRequest.POST,NetworkStateTextHandler.PATH);
		request.setBody(new StringBuilder("PIZZA"));
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_BAD_REQUEST;

		// Network config
		body = testHeadGetRequest(requestHandler, NetworkConfigJsonHandler.PATH, "application/json");

		// Network IO
		body = testHeadGetRequest(requestHandler, NetworkIOJsonHandler.PATH, "application/json");
		NetworkIO io = (NetworkIO) ObjectConstructor.fromJson(new Json(body));
		assert (float)io.getInput("Value") == 0F;
		assert (long)io.getInput("DateTime") == 0L;

		response = testPostRequest(requestHandler, NetworkIOJsonHandler.PATH, new Json());
		assert response.code == HttpURLConnection.HTTP_BAD_REQUEST;
		assert response.getBody().toString().equals("Failed to parse nl.zeesoft.zdk.neural.network.NetworkIO from JSON");
		
		// Network config
		response = testPostRequest(requestHandler, NetworkConfigJsonHandler.PATH, new Json());
		assert response.code == HttpURLConnection.HTTP_BAD_REQUEST;
		assert response.getBody().toString().equals("Failed to parse nl.zeesoft.zdk.neural.network.config.NetworkConfig from JSON");

		response = testPostRequest(requestHandler, NetworkConfigJsonHandler.PATH, JsonConstructor.fromObject(networkConfig));
		assert response.code == HttpURLConnection.HTTP_BAD_REQUEST;
		assert response.getBody().toString().equals(
			"A network must have at least one input\n" + 
			"A network must have at least one processor"
		);

		networkConfig = getSimpleNetworkConfig();
		request = new HttpRequest(HttpRequest.POST,NetworkConfigJsonHandler.PATH);
		request.setBody(JsonConstructor.fromObject(networkConfig));
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() == 0;

		// Test unavailable
		mockNetworkManager.setProcessing();
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;
		request = new HttpRequest(HttpRequest.POST,NetworkIOJsonHandler.PATH);
		request.setBody(JsonConstructor.fromObject(new NetworkIO()));
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;
		request = new HttpRequest(HttpRequest.GET,NetworkSettingsJsonHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;
		request = new HttpRequest(HttpRequest.POST,NetworkSettingsJsonHandler.PATH);
		NetworkSettings settings = new NetworkSettings();
		settings.processorLearning.put("Pooler", false);
		request.setBody(JsonConstructor.fromObject(settings));
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;
		request = new HttpRequest(HttpRequest.GET,NetworkStatsJsonHandler.PATH);
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;
		request = new HttpRequest(HttpRequest.POST,NetworkStateTextHandler.PATH);
		request.setBody(new StringBuilder("RESET"));
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_UNAVAILABLE;
		mockNetworkManager.unsetProcessing();
		
		// Network settings
		body = testHeadGetRequest(requestHandler, NetworkSettingsJsonHandler.PATH, "application/json");

		response = testPostRequest(requestHandler, NetworkSettingsJsonHandler.PATH, new Json());
		assert response.code == HttpURLConnection.HTTP_BAD_REQUEST;
		assert response.getBody().toString().equals("Failed to parse nl.zeesoft.zdk.app.neural.handlers.api.NetworkSettings from JSON");

		settings = new NetworkSettings();
		settings.workers = 3;
		settings.initTimeoutMs = 10101;
		settings.resetTimeoutMs = 10102;
		settings.processorLearning.put("Pooler", false);
		
		response = testPostRequest(requestHandler, NetworkSettingsJsonHandler.PATH, JsonConstructor.fromObject(settings));
		assert response.code == HttpURLConnection.HTTP_OK;
		assert response.getBody().length() == 0;

		body = testRequest(requestHandler,HttpRequest.GET,NetworkSettingsJsonHandler.PATH,"application/json");
		Json json = new Json(body);
		assert (int)json.root.get("workers").value == 3;
		assert (int)json.root.get("initTimeoutMs").value == 10101;
		assert (int)json.root.get("resetTimeoutMs").value == 10102;
		assert (boolean)json.root.get("processorLearning").get("keyValues").get(0).get("value").get("value").value == false;
		
		// Network IO
		body = testHeadGetRequest(requestHandler, NetworkIOJsonHandler.PATH, "application/json");

		io = new NetworkIO();
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
		body = testHeadGetRequest(requestHandler, NetworkStatsJsonHandler.PATH, "application/json");

		json = new Json(body);
		assert (int)json.root.get("proximalStats").get("activeSynapses").value >= 100000;
		assert (int)json.root.get("proximalStats").get("segments").value >= 1000;
		assert (int)json.root.get("proximalStats").get("synapses").value >= 100000;

		// Network IO stats
		body = testHeadGetRequest(requestHandler, NetworkIOStatsJsonHandler.PATH, "application/json");

		// Network IO accuracy
		body = testHeadGetRequest(requestHandler, NetworkIOAccuracyJsonHandler.PATH, "application/json");
		
		request = new HttpRequest("GET",NetworkIOAccuracyJsonHandler.PATH);
		request.query.append("max=100");
		response = requestHandler.handleRequest(request);
		assert response.code == HttpURLConnection.HTTP_OK;
		StrUtil.equals(response.getBody(),body);
		
		// Regular date time values training data
		body = testHeadGetRequest(requestHandler, RegularDateTimeValuesCsvHandler.PATH, "text/plain");
		assert body.length() == 91551;
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
	
	private static StringBuilder testHeadGetRequest(HttpRequestHandler requestHandler, String path, String expectedContentType) {
		testRequest(requestHandler,HttpRequest.HEAD,path,"");
		return testRequest(requestHandler,HttpRequest.GET,path,expectedContentType);
	}
	
	private static StringBuilder testRequest(HttpRequestHandler requestHandler, String method, String path, String expectedContentType) {
		Logger.debug(self, "Test " + method + " " + path);
		String query = "";
		if (path.contains("?")) {
			query = path.split("\\?")[1];
			path = path.split("\\?")[0];
		}
		HttpRequest request = new HttpRequest(method,path);
		request.query = new StringBuilder(query);
		HttpResponse response = requestHandler.handleRequest(request);
		if (response.code!=HttpURLConnection.HTTP_OK) {
			Logger.error(self, "Response code: " + response.code);
		}
		assert response.code == HttpURLConnection.HTTP_OK;
		StringBuilder body = response.getBody();
		if (expectedContentType.length()>0) {
			assert response.head.get(HttpHeader.CONTENT_TYPE).value.equals(expectedContentType);
			assert body.length() > 0;
		} else {
			assert body.length() == 0;
		}
		return response.getBody();
	}
	
	private static HttpResponse testPostRequest(HttpRequestHandler requestHandler, String path, Json body) {
		HttpRequest request = new HttpRequest(HttpRequest.POST,path);
		request.setBody(body);
		return requestHandler.handleRequest(request);
	}
}
