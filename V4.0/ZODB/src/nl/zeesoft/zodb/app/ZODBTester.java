package nl.zeesoft.zodb.app;

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
	
	protected void initializeRequestsNoLock() {
		DatabaseRequest req = null;
		DatabaseResponse res = null;
		DatabaseResult result = null;
		
		int num = 50;

		List<String> objectNames = new ArrayList<String>();
		for (int i = 1; i <= num; i++) {
			objectNames.add("SelfTestObject" + String.format("%03d",i));
		}
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_REMOVE);
		req.contains = "SelfTest";
		res = new DatabaseResponse();
		addRequestNoLock(req.toJson(),res.toJson());
		
		for (String name: objectNames) {
			req = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
			req.name = name;
			req.obj = getTestObject(req.name);
			res = new DatabaseResponse();
			addRequestNoLock(req.toJson(),res.toJson());
		}
		
		long id = 0;
		for (String name: objectNames) {
			id++;
			req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			req.name = name;
			res = new DatabaseResponse();
			result = new DatabaseResult();
			result.name = req.name;
			result.id = id;
			result.obj = getTestObject(req.name);
			res.results.add(result);
			addRequestNoLock(req.toJson(),res.toJson());
		}
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		req.contains = "SelfTest";
		res = new DatabaseResponse();
		id = 0;
		for (String name: objectNames) {
			id++;
			result = new DatabaseResult();
			result.name = name;
			result.id = id;
			res.results.add(result);
			if (id==10) {
				break;
			}
		}
		addRequestNoLock(req.toJson(),res.toJson());
	}
	
	private JsFile getTestObject(String name) {
		JsFile obj = new JsFile();
		obj.rootElement = new JsElem();
		obj.rootElement.children.add(new JsElem("testData",name,true));
		return obj;
	}
}
