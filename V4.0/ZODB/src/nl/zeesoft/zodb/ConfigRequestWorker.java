package nl.zeesoft.zodb;

import nl.zeesoft.zdk.json.JsAbleClientRequest;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;

public class ConfigRequestWorker extends Worker {
	private Config				config		= null;
	private DatabaseRequest		request		= null;
	private String 				url			= null;
	private JsClientListener	listener	= null;
	
	public ConfigRequestWorker(Config config,DatabaseRequest request,String url,JsClientListener listener) {
		super(config.getMessenger(),config.getUnion());
		setSleep(1);
		
		this.config = config;
		this.request = request;
		this.url = url;
		this.listener = listener;
		
		start();
	}
	
	@Override
	protected void whileWorking() {
		DatabaseResponse response = config.getZODB().handleRequest(request);
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
