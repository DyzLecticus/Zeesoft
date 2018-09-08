package nl.zeesoft.zevt.trans;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsClient;
import nl.zeesoft.zdk.json.JsClientRequest;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;

public class EntityClient extends JsClient {
	private EntityRequestResponse		request			= null;
	private EntityClientListener		listener		= null;
	
	public EntityClient(Config config) {
		super(config.getMessenger(),config.getUnion());
	}

	public void handleRequest(EntityRequestResponse request,String url,EntityClientListener listener) {
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
		EntityRequestResponse res = null;
		lockMe(this);
		if (response!=null && response.rootElement!=null) {
			res = new EntityRequestResponse();
			res.fromJson(response);	
		}
		listener.handledRequest(res, err, ex);
		this.request = null;
		this.listener = null;
		unlockMe(this);
	}
}
