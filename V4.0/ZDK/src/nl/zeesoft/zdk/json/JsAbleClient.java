package nl.zeesoft.zdk.json;

import java.util.List;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class JsAbleClient extends JsClient {
	public JsAbleClient(Messenger msgr,WorkerUnion uni) {
		super(msgr,uni);
	}

	public void handleRequest(JsAble request,String url,JsAble response) {
		JsClientWorker worker = new JsClientWorker(getMessenger(),getUnion(),this,new JsAbleClientRequest(request,url,response));
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
