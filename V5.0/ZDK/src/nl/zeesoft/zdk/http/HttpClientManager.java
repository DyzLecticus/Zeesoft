package nl.zeesoft.zdk.http;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.Lock;

public class HttpClientManager {
	private static final Lock				lock				= new Lock();
	private static final List<HttpClient> 	connectedClients	= new ArrayList<HttpClient>();
	
	public static void connectedClient(HttpClient client) {
		Object self = new HttpClientManager();
		lock.lock(self);
		connectedClients.add(client);
		lock.unlock(self);
	}
	
	public static void disconnectedClient(HttpClient client) {
		Object self = new HttpClientManager();
		lock.lock(self);
		connectedClients.remove(client);
		lock.unlock(self);
	}
	
	public static List<HttpClient> getConnectedClients() {
		Object self = new HttpClientManager();
		lock.lock(self);
		List<HttpClient> r = new ArrayList<HttpClient>(connectedClients);
		lock.unlock(self);
		return r;
	}
	
	public static List<CodeRunner> closeConnectedClients() {
		List<CodeRunner> r = new ArrayList<CodeRunner>();
		Object self = new HttpClientManager();
		lock.lock(self);
		for (HttpClient client: connectedClients) {
			client.disconnect();
			r.add(client.getRunner());
		}
		lock.unlock(self);
		return r;
	}
}
