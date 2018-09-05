package nl.zeesoft.zodb.test;

import java.io.File;
import java.util.Date;

import nl.zeesoft.zdk.ZStringBuilder;
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
		// TODO: Describe
		/*
		System.out.println("This test shows how to convert a *TestConfiguration* to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the test configuration");
		System.out.println("TestConfiguration tco = new TestConfiguration();");
		System.out.println("// Initialize the test configuration");
		System.out.println("tco.initialize();");
		System.out.println("// Convert the test configuration to JSON");
		System.out.println("JsFile json = tco.toJson();");
		System.out.println("// Convert the test configuration from JSON");
		System.out.println("tco.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestTestConfiguration.class));
		System.out.println(" * " + getTester().getLinkForClass(TestConfiguration.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		Config config = new Config();
		config.initialize(true,"dist/","",false);
		
		File dir = new File("dist/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		Database db = new Database(config);
		db.install();
		db.start();

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
			DatabaseResponse res = handleRequest(handler,req,1);
			if (res.results.size()>0) {
				assertEqual(res.results.get(0).name,"testObject001","Name of object found by id does not match expectation");
				assertEqual(res.results.get(0).obj!=null,true,"Index element found by id does not contain an object");
			}
			
			req = new DatabaseRequest(DatabaseRequest.TYPE_GET);
			req.name = "testObject125";
			res = handleRequest(handler,req,1);
			if (res.results.size()>0) {
				assertEqual(res.results.get(0).name,"testObject125","Name of object found by name does not match expectation");
				assertEqual(res.results.get(0).obj!=null,true,"Index element found by name does not contain an object");
			}
			
			req = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
			req.startsWith = "testObject1";
			req.max = 10;
			res = handleRequest(handler,req,10);
			if (res.results.size()>0) {
				assertEqual(res.results.get(0).name,"testObject100","Name of object found by startsWith does not match expectation");
			}
		}
				
		sleep(1000);
		
		db.stop();
		
		config.destroy();
	}
	
	private void addTestObjects(DatabaseRequestHandler handler) {
		for (int i = 1; i <= 250; i++) {
			DatabaseRequest req = new DatabaseRequest(DatabaseRequest.TYPE_ADD);
			req.name = "testObject" + String.format("%03d",i);
			JsFile obj = new JsFile();
			obj.rootElement = new JsElem();
			obj.rootElement.children.add(new JsElem("data",req.name,true));
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
	
	private DatabaseResponse handleRequest(DatabaseRequestHandler handler, DatabaseRequest req,int expectedResults) {
		DatabaseResponse res = handler.handleDatabaseRequest(req);
		if (res.errors.size()>0) {
			assertEqual(res.errors.get(0),new ZStringBuilder(),"The request returned an error");
		} else if (res.results.size()!=expectedResults) {
			assertEqual(res.results.size(),expectedResults,"The number of results does not match expectation");
		}
		return res;
	}
}
