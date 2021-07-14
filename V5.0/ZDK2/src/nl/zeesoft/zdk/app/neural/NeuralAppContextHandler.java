package nl.zeesoft.zdk.app.neural;

import java.net.HttpURLConnection;

import nl.zeesoft.zdk.app.AppContextHandler;
import nl.zeesoft.zdk.http.HttpResponse;

public class NeuralAppContextHandler extends AppContextHandler {
	public NeuralAppContextHandler(NeuralApp app) {
		super(app);
	}
	
	public NetworkManager getNetworkManager() {
		return ((NeuralApp)app).getNetworkManager();
	}
	
	public NetworkRecorder getNetworkRecorder() {
		return ((NeuralApp)app).getNetworkRecorder();
	}
	
	public boolean checkNetworkReady(HttpResponse response) {
		boolean r = getNetworkManager().isReady();
		if (!r) {
			setResponseUnavailable(response);
		}
		return r;
	}
	
	public void setResponseUnavailable(HttpResponse response) {
		response.code = HttpURLConnection.HTTP_UNAVAILABLE;
		response.message = "Service Unavailable";
	}
}
