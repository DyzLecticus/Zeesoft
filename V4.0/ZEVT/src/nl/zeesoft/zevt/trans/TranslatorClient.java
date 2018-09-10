package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsClient;
import nl.zeesoft.zdk.json.JsClientRequest;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;

public class TranslatorClient extends JsClient {
	private TranslatorRequestResponse		request			= null;
	private TranslatorClientListener		listener		= null;
	
	public TranslatorClient(Config config) {
		super(config.getMessenger(),config.getUnion());
	}

	public void handleRequest(TranslatorRequestResponse request,String url,TranslatorClientListener listener) {
		lockMe(this);
		if (this.request==null) {
			this.request = request;
			this.listener = listener;
		}
		unlockMe(this);
		handleRequest(request.toJson(),url);
	}
	
	@Override
	protected void requestIsDone(JsClientRequest request,JsFile response, ZStringBuilder err, Exception ex) {
		TranslatorRequestResponse res = null;
		lockMe(this);
		if (response!=null && response.rootElement!=null) {
			res = new TranslatorRequestResponse();
			res.fromJson(response);	
		}
		listener.handledRequest(res, err, ex);
		this.request = null;
		this.listener = null;
		unlockMe(this);
	}
}
