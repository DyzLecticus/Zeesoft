package nl.zeesoft.zdk.json;

import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * Thread safe JSON HTTP request handler for request objects that implement the JsAble interface.
 */
public class JsAbleClient extends JsClient {
	public JsAbleClient(Messenger msgr,WorkerUnion uni) {
		super(msgr,uni);
	}

	public void handleRequest(JsAble request,String url,JsAble response) {
		handleRequest(request,url,response,0);
	}
	
	public void handleRequest(JsAble request,String url,JsAble response,int timeoutSeconds) {
		JsAbleClientRequest req = new JsAbleClientRequest(request,url,response);
		if (timeoutSeconds>0) {
			req.timeoutSeconds = timeoutSeconds;
		}
		JsClientWorker worker = new JsClientWorker(getMessenger(),getUnion(),this,req);
		worker.start();
	}
	
	@Override
	protected void requestIsDone(List<JsClientListener> list,JsClientResponse response) {
		if (response.request instanceof JsAbleClientRequest) {
			JsAbleClientRequest request = (JsAbleClientRequest) response.request;
			request.resObject.fromJson(response.response);
		}
		super.requestIsDone(list, response);
	}
}
