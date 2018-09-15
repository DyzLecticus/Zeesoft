package nl.zeesoft.zdk.json;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.http.ZHttpRequest;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class JsClient extends Locker {
	private WorkerUnion				union			= null; 

	private int						maxRetries		= 60;
	private List<JsClientListener>	listeners		= new ArrayList<JsClientListener>();

	public JsClient(Messenger msgr,WorkerUnion uni) {
		super(msgr);
		union = uni;
	}
	
	public void setMaxRetries(int maxRetries) {
		lockMe(this);
		this.maxRetries = maxRetries;
		unlockMe(this);
	}
	
	public void addJsClientListener(JsClientListener listener) {
		lockMe(this);
		listeners.add(listener);
		unlockMe(this);
	}

	public void handleRequest(JsFile request,String url) {
		JsClientWorker worker = new JsClientWorker(getMessenger(),union,this,new JsClientRequest(request,url));
		worker.start();
	}
	
	protected boolean sendRequest(JsClientRequest request) {
		boolean done = false;
		
		ZStringBuilder err = new ZStringBuilder();
		Exception ex = null;
		
		lockMe(this);
		List<JsClientListener> list = new ArrayList<JsClientListener>(listeners);
		int max = maxRetries;
		unlockMe(this);
		
		ZStringBuilder req = request.request.toStringBuilder();
		ZHttpRequest http = new ZHttpRequest(null,"POST",request.url);
		JsFile res = http.sendJsonRequest(req);
		
		if (http.getResponseCode()==503) {
			request.retries++;
			if (request.retries>max) {
				err.append("Server at " + request.url + " is not ready");
				done = true;
			}
		} else {
			if (res.rootElement==null) {
				err.append("Failed to obtain response from " + request.url);
				ex = http.getException();
			}
			done = true;
		}
		
		if (done) {
			JsClientResponse response = new JsClientResponse();
			response.request = request;
			response.response = res;
			response.error = err;
			response.ex = ex;
			requestIsDone(list,response);
		}
		return done;
	}
	
	protected void requestIsDone(List<JsClientListener> list,JsClientResponse response) {
		for (JsClientListener listener: list) {
			try {
				listener.handledRequest(response);
			} catch (Exception ex) {
				if (getMessenger()!=null) {
					getMessenger().error(listener,"Exception while notifying listeners",ex);
				}
			}
		}
	}
	
	protected WorkerUnion getUnion() {
		return union;
	}
}
