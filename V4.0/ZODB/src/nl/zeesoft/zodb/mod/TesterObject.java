package nl.zeesoft.zodb.mod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZDate;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsClient;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zodb.Config;

public abstract class TesterObject extends Locker implements JsClientListener {
	private Config					configuration	= null;
	private String					url				= "";
	private JsClient				client			= null;
	
	private List<TesterRequest>		requests		= new ArrayList<TesterRequest>();
	private boolean					testing			= false;
	private int						todo			= 0;
	
	private List<ZStringBuilder>	logLines		= new ArrayList<ZStringBuilder>();
	
	private JsFile					results			= null;
	
	public TesterObject(Config config,String url) {
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
				logLines.clear();
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

	public void stop() {
		boolean r = false;
		lockMe(this);
		if (testing) {
			testing = false;
			todo = 0;
			r = true;
		}
		unlockMe(this);
		if (r) {
			configuration.debug(this,"Stopped testing " + url);
		}
	}

	public boolean isTesting() {
		boolean r = false;
		lockMe(this);
		r = testing;
		unlockMe(this);
		return r;
	}
	
	@Override
	public void handledRequest(JsClientResponse response) {
		lockMe(this);
		if (testing) {
			int i = requests.size() - todo;
			handledRequestNoLock(requests.get(i),response.response,response.error);
			if (response.error.length()>0) {
				configuration.error(this,response.error.toString(),response.ex);
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

	protected Config getConfiguration() {
		return configuration;
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
		json.rootElement.children.add(new JsElem("url",url,true));
		json.rootElement.children.add(new JsElem("tests","" + requests.size()));
		json.rootElement.children.add(new JsElem("successFull","" + successFull));
		json.rootElement.children.add(new JsElem("averageResponseMs","" + avgTime));
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
		JsElem logsElem = new JsElem("log",true);
		json.rootElement.children.add(logsElem);
		for (ZStringBuilder line: logLines) {
			logsElem.children.add(new JsElem(null,line,true));
		}
		setResultsNolock(json);
	}
	
	protected void handleRequestNoLock(TesterRequest request) {
		request.time = (new Date()).getTime();
		client.handleRequest(request.request, url);
	}

	protected void handledRequestNoLock(TesterRequest request, JsFile response, ZStringBuilder error) {
		request.time = (new Date()).getTime() - request.time;
		request.response = getCheckResponseNoLock(response);
		request.error = error;
		if (error.length()==0) {
			checkResponseNoLock(request);
		}
		addLogLineForRequest(request);
	}
	
	protected JsFile getCheckResponseNoLock(JsFile response) {
		return response;
	}
	
	protected void checkResponseNoLock(TesterRequest request) {
		ZStringBuilder res = request.response.toStringBuilder();
		ZStringBuilder exp = request.expectedResponse.toStringBuilder();
		if (!res.equals(exp)) {
			request.error = new ZStringBuilder("Response does not match expectation");
		}
	}
	
	protected abstract void initializeRequestsNoLock();

	protected void addRequestNoLock(JsAble request,JsAble expectedResponse) {
		addRequestNoLock(request.toJson(),expectedResponse.toJson());
	}

	protected void addRequestNoLock(JsFile request,JsFile expectedResponse) {
		TesterRequest req = new TesterRequest();
		req.request = request;
		req.expectedResponse = expectedResponse;
		requests.add(req);
	}
	
	protected void addLogLineForRequest(TesterRequest request) {
		addLogLineNoLock("Request took: " + request.time + " ms");
	}
	
	protected void addLogLineNoLock(String str) {
		ZStringBuilder line = new ZStringBuilder();
		ZDate date = new ZDate();
		line.append(date.getDateTimeString());
		line.append(": ");
		line.append(str);
		logLines.add(line);
	}
}
