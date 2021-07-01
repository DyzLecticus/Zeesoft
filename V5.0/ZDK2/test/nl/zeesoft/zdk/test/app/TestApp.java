package nl.zeesoft.zdk.test.app;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.app.App;
import nl.zeesoft.zdk.app.AppConfig;
import nl.zeesoft.zdk.app.AppContextHandler;
import nl.zeesoft.zdk.app.AppStateManager;
import nl.zeesoft.zdk.http.HttpContextRequestHandler;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.http.HttpServerConfig;

public class TestApp {
	protected static TestApp	self		= new TestApp();
	
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
		
		AppConfig config = new AppConfig();
		App app = new App(config);
		
		// Test context handlers
		HttpServerConfig serverConfig = config.loadHttpServerConfig(app);
		HttpContextRequestHandler requestHandler = (HttpContextRequestHandler) serverConfig.getRequestHandler();
		
		AppContextHandler handler = (AppContextHandler) requestHandler.get("/");
		assert handler.getServer() == null;
		assert !handler.getNetwork().isInitialized();
		
		HttpRequest request = new HttpRequest(HttpRequest.GET,"/");
		HttpResponse response = new HttpResponse();
		handler.handleRequest(request, response);
		assert response.getBody().length() > 0;

		request = new HttpRequest(HttpRequest.POST,"/");
		response = new HttpResponse();
		handler.handleRequest(request, response);
		assert response.getBody().length() == 0;
		assert response.code == HttpURLConnection.HTTP_NOT_IMPLEMENTED;
		
		// Test app
		assert app.start();
		assert handler.getServer().isOpen();
		assert handler.getNetwork().isInitialized();
		assert app.getState().equals(AppStateManager.STARTED);
		assert app.isStarted();
		assert !app.start();
		assert app.stop();
		assert !handler.getServer().isOpen();
		assert !app.isStarted();
		assert !app.stop();
	}
}
