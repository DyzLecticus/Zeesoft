package nl.zeesoft.zodb.db;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.http.ZHttpRequest;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.app.handler.JsonZODBRequestHandler;

public class Client extends Locker {
	private Config					configuration	= null;
	private String					url				= "";

	private int						maxRetries		= 60;
	private List<ClientListener>	listeners		= new ArrayList<ClientListener>();

	private DatabaseRequest			request			= null;
	private int						retryCount		= 0;
	
	public Client(Config config) {
		super(config.getMessenger());
		this.configuration = config;
		url = configuration.getZODB().url + JsonZODBRequestHandler.PATH;
	}
	
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}
	
	public void addListener(ClientListener listener) {
		listeners.add(listener);
	}

	public boolean isHandlingRequest() {
		boolean r = false;
		lockMe(this);
		if (request!=null) {
			r = true;
		}
		unlockMe(this);
		return r;
	}

	public boolean handleRequest(DatabaseRequest request) {
		boolean handling = false;
		lockMe(this);
		if (this.request==null) {
			this.request = request;
			retryCount = 0;
			ClientWorker worker = new ClientWorker(configuration.getMessenger(),configuration.getUnion(),this);
			worker.start();
			handling = true;
		}
		unlockMe(this);
		return handling;
	}
	
	protected boolean handleRequest() {
		boolean done = false;
		
		DatabaseResponse res = null;
		ZStringBuilder err = new ZStringBuilder();
		Exception ex = null;

		lockMe(this);
		List<ClientListener> lsnrs = new ArrayList<ClientListener>(listeners);
		ZStringBuilder req = request.toJson().toStringBuilder();
		unlockMe(this);
		
		ZHttpRequest http = new ZHttpRequest(null,"POST",url);
		JsFile json = http.sendJsonRequest(req);
		
		if (http.getResponseCode()==503) {
			lockMe(this);
			retryCount++;
			if (retryCount>=maxRetries) {
				err.append("Server at " + url + " is not ready");
				done = true;
			}
			unlockMe(this);
		} else {
			if (json.rootElement==null) {
				err.append("Failed to obtain database response from " + url);
				ex = http.getException();
				done = true;
			} else {
				res = new DatabaseResponse();
				lockMe(this);
				res.request = request;
				unlockMe(this);
				res.fromJson(json);
				done = true;
			}
		}
		
		if (done) {
			lockMe(this);
			request = null;
			unlockMe(this);
			for (ClientListener listener: lsnrs) {
				listener.handledRequest(res,err,ex);
			}
		}
		return done;
	}
}
