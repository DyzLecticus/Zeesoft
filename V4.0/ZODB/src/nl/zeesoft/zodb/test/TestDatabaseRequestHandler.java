package nl.zeesoft.zodb.test;

import java.util.Date;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseRequestHandler;
import nl.zeesoft.zodb.db.DatabaseResponse;
import nl.zeesoft.zodb.db.IndexElement;

public class TestDatabaseRequestHandler extends TestObject {
	public TestDatabaseRequestHandler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDatabaseRequestHandler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test can be used to test the ZODB *DatabaseRequestHandler*.");
	}
	
	@Override
	protected void test(String[] args) {
		Config config = new Config();
		
		Database db = TestDatabase.initializeTestDatabase(config,null);
				
		sleep(1000);
		
		assertEqual(db.isOpen(),true,"Failed to initialize database within one second");
		if (db.isOpen()) {
			
			DatabaseRequestHandler handler = new DatabaseRequestHandler(db);
			
			IndexElement element = db.getObjectById(1L);
			if (element==null) {
				Date started = new Date();
				addTestObjects(handler);
				System.out.println("Adding 250 objects took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			}

			DatabaseRequest req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			req.id = 1;
			DatabaseResponse res = handleRequest(handler,req,1,null);
			if (res.results.size()>0) {
				assertEqual(res.results.get(0).name.toString(),"testObject001","Name of object found by id does not match expectation");
				assertEqual(res.results.get(0).obj!=null,true,"Index element found by id does not contain an object");
			}

			req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			req.id = 1;
			req.encoding = DatabaseRequest.ENC_ASCII;
			res = handleRequest(handler,req,1,null);
			if (res.results.size()>0) {
				assertEqual(res.results.get(0).encoded,new ZStringBuilder("37,67,-19,54,44,72,60,-16,5,-8,-19,78,64,71,63,21,45,50,64,61,63,-4,-5,-1,-3,-12,-19,64,64,65,-3,8,-21,-6,-4,-4,88"),"ASCII encoded object does not match expectation");
			}
			
			req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			req.id = 1;
			req.encoding = DatabaseRequest.ENC_KEY;
			res = handleRequest(handler,req,1,null);
			if (res.results.size()>0) {
				assertEqual(res.results.get(0).encoded,new ZStringBuilder("UWpWhY5Ze1L1E1S2h3o3NWrXwYnZDZM1L2z3j4C4KVAWvX7Y8ZNZP2M3t4o3UVnWgXlYXZd20"),"KEY encoded object does not match expectation");
			}
			
			req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			req.name = new ZStringBuilder("testObject125");
			res = handleRequest(handler,req,1,null);
			if (res.results.size()>0) {
				assertEqual(res.results.get(0).name.toString(),"testObject125","Name of object found by name does not match expectation");
				assertEqual(res.results.get(0).obj!=null,true,"Index element found by name does not contain an object");
			}
			
			req = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
			req.startsWith = new ZStringBuilder("testObject1");
			res = handleRequest(handler,req,10,null);
			if (res.results.size()>0) {
				assertEqual(res.results.get(0).name.toString(),"testObject100","Name of object found by startsWith does not match expectation");
			}

			req = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
			req.start = 20;
			req.index = "testObject:data";
			req.ascending = false;
			res = handleRequest(handler,req,10,null);

			req = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
			req.modAfter = (new Date()).getTime();
			req.modBefore = req.modAfter - 1;
			res = handleRequest(handler,req,10,"Request modAfter must be lower than modBefore");
			
			ZStringEncoder encoder = new ZStringEncoder("{\"data\":\"changedObject002\",\"num\":2}");
			encoder.encodeAscii();
			req = new DatabaseRequest(DatabaseRequest.TYPE_SET);
			req.id = 2;
			req.encoding = DatabaseRequest.ENC_ASCII;
			req.encoded = encoder; 
			res = handleRequest(handler,req,0,null);
			
			req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			req.id = 2;
			res = handleRequest(handler,req,1,null);
			if (res.results.size()>0) {
				assertEqual(res.results.get(0).obj.rootElement.children.get(0).value,new ZStringBuilder("changedObject002"),"Object with id 2 was not changed as expected");
			}
			
			encoder = new ZStringEncoder("{\"data\":\"changedObject003\",\"num\":3}");
			encoder.encodeKey(config.getZODBKey(),0);
			req = new DatabaseRequest(DatabaseRequest.TYPE_SET);
			req.id = 3;
			req.encoding = DatabaseRequest.ENC_KEY;
			req.encoded = encoder; 
			res = handleRequest(handler,req,0,null);
			
			req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			req.id = 3;
			res = handleRequest(handler,req,1,null);
			if (res.results.size()>0) {
				assertEqual(res.results.get(0).obj.rootElement.children.get(0).value,new ZStringBuilder("changedObject003"),"Object with id 3 was not changed as expected");
			}
		}
				
		sleep(1000);

		config.destroy();
	}
	
	private void addTestObjects(DatabaseRequestHandler handler) {
		for (int i = 1; i <= 250; i++) {
			DatabaseRequest req = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
			req.name.append("testObject");
			req.name.append(String.format("%03d",i));
			JsFile obj = new JsFile();
			obj.rootElement = new JsElem();
			obj.rootElement.children.add(new JsElem("data",req.name,true));
			obj.rootElement.children.add(new JsElem("num","" + i,true));
			req.obj = obj;
			DatabaseResponse res = handler.handleDatabaseRequest(req);
			assertEqual(res.errors.size(),0,"Response errors does not match expectation");
			if (res.errors.size()>0) {
				System.err.println("ERROR: " + res.errors.get(0));
			} else if (res.results.size()>0) {
				assertEqual((int) res.results.get(0).id,i,"Object id does not match expectation");
			}
		}
	}
	
	private DatabaseResponse handleRequest(DatabaseRequestHandler handler, DatabaseRequest req,int expectedResults,String expectedError) {
		DatabaseResponse res = handler.handleDatabaseRequest(req);
		if (res.errors.size()>0) {
			if (expectedError!=null && expectedError.length()>0) {
				assertEqual(res.errors.get(0),new ZStringBuilder(expectedError),"Request error does not match expectation");
			} else {
				assertEqual(res.errors.get(0),new ZStringBuilder(),"The request returned an error");
			}
		} else if (res.results.size()!=expectedResults) {
			if (expectedError!=null && expectedError.length()>0) {
				assertEqual(res.errors.size()>0,true,"Failed invoke response error");
			} else {
				assertEqual(res.results.size(),expectedResults,"The number of results does not match expectation");
			}
		}
		return res;
	}
}
