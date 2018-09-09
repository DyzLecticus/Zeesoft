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
import nl.zeesoft.zodb.db.DatabaseResult;

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
		if (r) {
			configuration.debug(this,"Testing " + url + " ...");
		}
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
		if (err.length()>0) {
			configuration.error(this,err.toString(),ex);
			todo = 0;
			testing = false;
		} else {
			todo--;
			if (todo>0) {
				i++;
				handleRequestNoLock(requests.get(i));
			} else {
				createResultsNoLock(requests);
				testing = false;
				configuration.debug(this,"Tested " + url);
			}
		}
		unlockMe(this);
	}
	
	public JsFile getResults() {
		JsFile r = null;
		lockMe(this);
		r = results;
		unlockMe(this);
		return r;
	}

	protected void setResultsNolock(JsFile results) {
		this.results = results;
	}

	protected void createResultsNoLock(List<TesterRequest> requests) {
		long totalTime = 0;
		int successFull = 0;
		for (TesterRequest request: requests) {
			totalTime += request.time;
			if (request.error.length()==0) {
				successFull++;
			}
		}
		long avgTime = totalTime / requests.size();
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("tests","" + requests.size()));
		json.rootElement.children.add(new JsElem("successFull","" + successFull));
		json.rootElement.children.add(new JsElem("averageResponseTime","" + avgTime));
		JsElem errsElem = new JsElem("errors",true);
		json.rootElement.children.add(errsElem);
		for (TesterRequest request: requests) {
			if (request.error.length()>0) {
				JsElem errElem = new JsElem();
				errsElem.children.add(errElem);
				errElem.children.add(new JsElem("error",request.error,true));
				
				JsElem reqElem = new JsElem("request");
				errElem.children.add(reqElem);
				reqElem.children = request.request.rootElement.children;
				
				JsElem resElem = new JsElem("response");
				errElem.children.add(resElem);
				if (request.response!=null && request.response.rootElement!=null) {
					resElem.children = request.response.rootElement.children;
				}
				
				JsElem expElem = new JsElem("expectedResponse");
				errElem.children.add(expElem);
				expElem.children = request.expectedResponse.rootElement.children;
			}
		}
		setResultsNolock(json);
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
		DatabaseRequest req = null;
		DatabaseResponse res = null;
		DatabaseResult result = null;

		req = new DatabaseRequest(DatabaseRequest.TYPE_REMOVE);
		req.contains = "SelfTest";
		res = new DatabaseResponse();
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
		result = new DatabaseResult();
		result.name = req.name;
		result.id = 1;
		result.obj = getTestObject(req.name);
		res.results.add(result);
		addRequestNoLock(req,res);
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		req.name = "SelfTestObject2";
		res = new DatabaseResponse();
		result = new DatabaseResult();
		result.name = req.name;
		result.id = 2;
		result.obj = getTestObject(req.name);
		res.results.add(result);
		addRequestNoLock(req,res);
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		req.contains = "SelfTest";
		res = new DatabaseResponse();
		res = new DatabaseResponse();
		result = new DatabaseResult();
		result.name = "SelfTestObject1";
		result.id = 1;
		res.results.add(result);
		result = new DatabaseResult();
		result.name = "SelfTestObject2";
		result.id = 2;
		res.results.add(result);
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
