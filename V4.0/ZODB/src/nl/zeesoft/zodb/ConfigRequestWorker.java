package nl.zeesoft.zodb;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAbleClientRequest;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;

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
		if (config.getZODB().getDatabase().isOpen()) {
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
		} else {
			tries++;
			if (tries >= timeoutSeconds * 10) {
				DatabaseResponse response = new DatabaseResponse();
				response.request = request;
				response.errors.add(new ZStringBuilder("Request timed out; database is not open for business"));
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
	}
}
