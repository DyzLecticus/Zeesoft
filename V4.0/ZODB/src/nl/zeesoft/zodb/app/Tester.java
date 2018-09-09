package nl.zeesoft.zodb.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsClient;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientRequest;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;

public class Tester extends Locker implements JsClientListener {
	private Config					configuration	= null;
	private String					url				= "";
	private JsClient				client			= null;
	
	private List<TesterRequest>		requests		= new ArrayList<TesterRequest>();
	private boolean					testing			= false;
	private int						todo			= 0;
	
	private JsFile					results			= null;
	
	public Tester(Config config,String url) {
		super(config.getMessenger());
		this.configuration = config;
		this.url = url;
		this.client = new JsClient(config.getMessenger(),config.getUnion());
		client.addJsClientListener(this);
	}
	
	public boolean start() {
		boolean r = false;
		lockMe(this);
		if (!testing) {
			requests.clear();
			initializeRequestsNoLock();
			if (requests.size()>0) {
				testing = true;
				todo = requests.size();
				r = true;
				handleRequestNoLock(requests.get(0));
			}
		}
		unlockMe(this);
		return r;
	}

	public boolean isTesting() {
		boolean r = false;
		lockMe(this);
		r = testing;
		unlockMe(this);
		return r;
	}
	
	@Override
	public void handledRequest(JsClientRequest request, JsFile response, ZStringBuilder err, Exception ex) {
		lockMe(this);
		int i = requests.size() - todo;
		handledRequestNoLock(requests.get(i),response,err);
		unlockMe(this);
		if (err.length()>0) {
			configuration.error(this,err.toString(),ex);
		} else {
			lockMe(this);
			todo--;
			if (todo>0) {
				i++;
				handleRequestNoLock(requests.get(i));
			} else {
				createResultsNoLock(requests);
				testing = false;
			}
			unlockMe(this);
		}
	}
	
	public JsFile getResults() {
		JsFile r = null;
		lockMe(this);
		r = results;
		lockMe(this);
		return r;
	}

	protected void setResultsNolock(JsFile results) {
		this.results = results;
	}

	protected void createResultsNoLock(List<TesterRequest> requests) {
		// TODO: Create results
		setResultsNolock(new JsFile());
	}
	
	protected void handleRequestNoLock(TesterRequest request) {
		request.time = (new Date()).getTime();
		client.handleRequest(request.request, url);
	}

	protected void handledRequestNoLock(TesterRequest request, JsFile response, ZStringBuilder error) {
		request.time = (new Date()).getTime() - request.time;
		request.response = response;
		request.error = error;
		if (error.length()==0) {
			checkResponse(request,response);
		}
	}
	
	protected void checkResponse(TesterRequest request, JsFile response) {
		ZStringBuilder res = response.toStringBuilder();
		ZStringBuilder exp = request.expectedResponse.toStringBuilder();
		if (!res.equals(exp)) {
			request.error = new ZStringBuilder("Response does not match expectation");
		}
	}
	
	protected void initializeRequestsNoLock() {
		DatabaseRequest req = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		DatabaseResponse res = new DatabaseResponse();
		addRequestNoLock(req,res);
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
		req.name = "SelfTestObject1";
		req.obj = getTestObject(req.name);
		res = new DatabaseResponse();
		addRequestNoLock(req,res);
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
		req.name = "SelfTestObject2";
		req.obj = getTestObject(req.name);
		res = new DatabaseResponse();
		addRequestNoLock(req,res);
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		req.name = "SelfTestObject1";
		res = new DatabaseResponse();
		addRequestNoLock(req,res);
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		req.name = "SelfTestObject2";
		res = new DatabaseResponse();
		addRequestNoLock(req,res);
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_REMOVE);
		req.id = 1;
		res = new DatabaseResponse();
		addRequestNoLock(req,res);
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_REMOVE);
		req.id = 2;
		res = new DatabaseResponse();
		addRequestNoLock(req,res);
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		res = new DatabaseResponse();
		addRequestNoLock(req,res);
	}
	
	protected void addRequestNoLock(DatabaseRequest req, DatabaseResponse res) {
		addRequestNoLock(req.toJson(),res.toJson());
	}
	
	protected void addRequestNoLock(JsFile request,JsFile expectedResponse) {
		TesterRequest req = new TesterRequest();
		req.request = request;
		req.expectedResponse = expectedResponse;
		requests.add(req);
	}
	
	private JsFile getTestObject(String name) {
		JsFile obj = new JsFile();
		obj.rootElement = new JsElem();
		obj.rootElement.children.add(new JsElem("testData",name,true));
		return obj;
	}
}
