package nl.zeesoft.zdk.json;

import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Worker;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class JsClientWorker extends Worker {
	private JsClient		client	= null;
	private JsClientRequest	request	= null;

	protected JsClientWorker(Messenger msgr, WorkerUnion union, JsClient client, JsClientRequest request) {
		super(msgr, union);
		this.client = client;
		this.request = request;
		setSleep(1000);
	}

	@Override
	public void whileWorking() {
		if (client.sendRequest(request)) {
			client = null;
			request = null;
			stop();
		}
	}
}
