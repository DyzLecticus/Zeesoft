package nl.zeesoft.zodb;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAbleClientRequest;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.mod.ModZODB;

public class ConfigRequestWorker extends Worker {
	private Config				config			= null;
	private DatabaseRequest		request			= null;
	private String 				url				= null;
	private int					timeoutSeconds	= 0;
	private JsClientListener	listener		= null;
	
	private int 				tries 			= 0;
	
	public ConfigRequestWorker(Config config,DatabaseRequest request,String url,int timeoutSeconds,JsClientListener listener) {
		super(config.getMessenger(),config.getUnion());
		setSleep(100);
		
		this.config = config;
		this.request = request;
		this.url = url;
		this.timeoutSeconds = timeoutSeconds;
		this.listener = listener;
		
		start();
	}
	
	@Override
	protected void whileWorking() {
		ModZODB zodb = config.getZODB();
		Database db = null;
		if (zodb!=null) {
			db = zodb.getDatabase();
		}
		if (db==null) {
			handledRequestError("Database is not available");
		} else if (db.isOpen()) {
			DatabaseResponse response = config.getZODB().handleRequest(request);
			handledRequest(response);
		} else {
			tries++;
			if (tries >= timeoutSeconds * 10) {
				handledRequestError("Request timed out; database is not open for business");
			}
		}
	}
	
	protected void handledRequestError(String msg) {
		DatabaseResponse response = new DatabaseResponse();
		response.request = request;
		response.errors.add(new ZStringBuilder(msg));
		handledRequest(response);
	}
	
	protected void handledRequest(DatabaseResponse response) {
		response.request = request;
		JsAbleClientRequest req = new JsAbleClientRequest(request,url,response);
		JsClientResponse res = new JsClientResponse();
		res.request = req;
		res.response = response.toJson();
		listener.handledRequest(res);
		
		config = null;
		request = null;
		url = null;
		listener = null;

		stop();
	}
}
