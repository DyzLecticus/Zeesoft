package nl.zeesoft.zodb.mod;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
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

		List<ZStringBuilder> objectNames = new ArrayList<ZStringBuilder>();
		for (int i = 1; i <= num; i++) {
			objectNames.add(new ZStringBuilder("Object" + String.format("%03d",i)));
		}
		
		req = new DatabaseRequest(DatabaseRequest.TYPE_REMOVE);
		req.contains = new ZStringBuilder(ModZODB.NAME + "/Objects/");
		res = new DatabaseResponse();
		addRequestNoLock(req,res);
		
		int n = 0;
		for (ZStringBuilder name: objectNames) {
			n++;
			req = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
			req.name = getObjectName(name);
			req.obj = getTestObject(name);
			if (n>=(num - 20)) {
				ZStringEncoder encoder = new ZStringEncoder(req.obj.toStringBuilder());
				req.obj = null;
				if (n>=(num - 10)) {
					req.encoding = DatabaseRequest.ENC_KEY;
					encoder.encodeKey(getConfiguration().getZODBKey(),0);
					req.encoded = encoder;
				} else if (n>=(num - 20)) {
					req.encoding = DatabaseRequest.ENC_ASCII;
					encoder.encodeAscii();
					req.encoded = encoder;
				}
			}
			res = new DatabaseResponse();
			addRequestNoLock(req,res);
		}
		
		long id = 0;
		for (ZStringBuilder name: objectNames) {
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
			if (id>=10) {
				break;
			}
		}

		ZStringBuilder objName = objectNames.get(0);
		req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		req.name = getObjectName(objName);
		req.encoding = DatabaseRequest.ENC_ASCII;
		ZStringEncoder encoder = new ZStringEncoder(getTestObject(objName).toStringBuilder());
		encoder.encodeAscii();
		res = new DatabaseResponse();
		result = new DatabaseResult();
		result.name = req.name;
		result.id = 1;
		result.encoded = encoder;
		res.results.add(result);
		addRequestNoLock(req,res);

		req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
		req.name = getObjectName(objName);
		req.encoding = DatabaseRequest.ENC_KEY;
		encoder = new ZStringEncoder(getTestObject(objName).toStringBuilder());
		encoder.encodeKey(getConfiguration().getZODBKey(),0);
		res = new DatabaseResponse();
		result = new DatabaseResult();
		result.name = req.name;
		result.id = 1;
		result.encoded = encoder;
		res.results.add(result);
		addRequestNoLock(req,res);

		req = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		req.contains = new ZStringBuilder(ModZODB.NAME + "/Objects/");
		res = new DatabaseResponse();
		res.size = num;
		id = 0;
		for (ZStringBuilder name: objectNames) {
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

		req = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
		req.index = ModZODB.NAME + "/Objects/:testData";
		req.ascending = false;
		req.start = 20;
		res = new DatabaseResponse();
		res.size = num;
		for (int i = 29; i >= 20; i--) {
			ZStringBuilder name = objectNames.get(i);
			result = new DatabaseResult();
			result.name = getObjectName(name);
			result.id = (long) (i + 1);
			res.results.add(result);
		}
		addRequestNoLock(req,res);
	}

	@Override
	protected JsFile getCheckResponseNoLock(JsFile response) {
		DatabaseResponse res = new DatabaseResponse();
		res.statusCode = 400;
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
	
	private JsFile getTestObject(ZStringBuilder name) {
		JsFile obj = new JsFile();
		obj.rootElement = new JsElem();
		obj.rootElement.children.add(new JsElem("testData",name,true));
		return obj;
	}
	
	private ZStringBuilder getObjectName(ZStringBuilder name) {
		return new ZStringBuilder(ModZODB.NAME + "/Objects/" + name);
	}
}
