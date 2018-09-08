package nl.zeesoft.zodb.db;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsClient;
import nl.zeesoft.zdk.json.JsClientRequest;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;

public class DatabaseClient extends JsClient {
	private DatabaseRequest		request			= null;
	private DatabaseClientListener		listener		= null;
	
	public DatabaseClient(Config config) {
		super(config.getMessenger(),config.getUnion());
	}

	public void handleRequest(DatabaseRequest request,String url,DatabaseClientListener listener) {
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
		DatabaseResponse res = null;
		lockMe(this);
		if (response!=null && response.rootElement!=null) {
			res = new DatabaseResponse();
			res.fromJson(response);	
			res.request = this.request;
		}
		listener.handledRequest(res, err, ex);
		this.request = null;
		this.listener = null;
		unlockMe(this);
	}
}
