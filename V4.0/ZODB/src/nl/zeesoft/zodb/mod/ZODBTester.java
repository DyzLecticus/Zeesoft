package nl.zeesoft.zodb.mod;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.DatabaseResult;

public class ZODBTester extends TesterObject {
	public ZODBTester(Config config,String url) {
		super(config,url);
	}
	
	@Override
	protected void initializeRequestsNoLock() {
		DatabaseRequest req = null;
		DatabaseResponse res = null;
		DatabaseResult result = null;
		
		int num = 50;

		List<String> objectNames = new ArrayList<String>();
		for (int i = 1; i <= num; i++) {
			objectNames.add("Object" + String.format("%03d",i));
		}
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_REMOVE);
		req.contains = ModZODB.NAME + "/Objects/";
		res = new DatabaseResponse();
		addRequestNoLock(req,res);
		
		for (String name: objectNames) {
			req = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
			req.name = getObjectName(name);
			req.obj = getTestObject(name);
			res = new DatabaseResponse();
			addRequestNoLock(req,res);
		}
		
		long id = 0;
		for (String name: objectNames) {
			id++;
			req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			req.name = getObjectName(name);
			res = new DatabaseResponse();
			result = new DatabaseResult();
			result.name = req.name;
			result.id = id;
			result.obj = getTestObject(name);
			res.results.add(result);
			addRequestNoLock(req,res);
		}
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		req.contains = ModZODB.NAME + "/Objects/";
		res = new DatabaseResponse();
		res.size = num;
		id = 0;
		for (String name: objectNames) {
			id++;
			result = new DatabaseResult();
			result.name = getObjectName(name);
			result.id = id;
			res.results.add(result);
			if (id==req.max) {
				break;
			}
		}
		addRequestNoLock(req,res);
	}

	@Override
	protected JsFile getCheckResponseNoLock(JsFile response) {
		DatabaseResponse res = new DatabaseResponse();
		res.fromJson(response);
		if (res.results.size()>0) {
			for (DatabaseResult result: res.results) {
				result.modified = 0;
			}
		}
		return res.toJson();
	}
	
	@Override
	protected void addLogLineForRequest(TesterRequest request) {
		DatabaseRequest req = new DatabaseRequest();
		req.fromJson(request.request);
		DatabaseResponse res = new DatabaseResponse();
		res.fromJson(request.response);
		if (res.results.size()>0) {
			addLogLineNoLock(req.type + " request took: " + request.time + " ms, results: " + res.results.size());
		} else if (req.type.length()>0) {
			addLogLineNoLock(req.type + " request took: " + request.time + " ms");
		}
	}
	
	private JsFile getTestObject(String name) {
		JsFile obj = new JsFile();
		obj.rootElement = new JsElem();
		obj.rootElement.children.add(new JsElem("testData",name,true));
		return obj;
	}
	
	private String getObjectName(String name) {
		return ModZODB.NAME + "/Objects/" + name;
	}
}