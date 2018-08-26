package nl.zeesoft.zodb.test;

import java.io.File;
import java.util.Date;

import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.IndexElement;

public class TestDatabase extends TestObject {
	public TestDatabase(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDatabase(new Tester())).test(args);
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
		db.open();

		sleep(1000);
		
		assertEqual(db.isOpen(),true,"Failed to initialize database within one second");
		if (db.isOpen()) {
			
			IndexElement element = null;
			
			element = db.getObjectById(1L);
			if (element==null) {
				Date started = new Date();
				addTestObjects(db);
				System.out.println("Adding 250 objects took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			}
			
			element = db.getObjectById(1L);
			if (element!=null) {
				assertEqual(element.name,"testObject1","Name of object found by id does not match expectation");
			} else {
				assertEqual(false,true,"Object with id 1 not found");
			}
			
			element = db.getObjectByName("testObject2");
			if (element!=null) {
				assertEqual(element.name,"testObject2","Name of object found by name does not match expectation");
			} else {
				assertEqual(false,true,"Object with name 'testObject2' not found");
			}
			
			sleep(1000);
			
			element = db.getObjectByName("testObject125");
			if (element!=null) {
				element = db.removeObject(element.id);
				if (element!=null) {
					assertEqual(element.name,"testObject125","Removed object name does not match expectation");
					element = addTestObject(db,"testObject125");
					assertEqual(element!=null,true,"Failed to add object with name 'testObject125'");
				}
			} else {
				element = addTestObject(db,"testObject125");
				assertEqual(element!=null,true,"Failed to add object with name 'testObject125'");
			}
		}
				
		sleep(1000);
		
		db.close();
		
		config.destroy();
	}
	
	private void addTestObjects(Database db) {
		for (int i = 1; i < 250; i++) {
			IndexElement element = addTestObject(db,"testObject" + i);
			assertEqual((int) element.id,i,"Object id does not match expectation");
		}
	}

	private IndexElement addTestObject(Database db,String name) {
		return db.addObject(name,getTestObject(name));
	}
	
	private JsFile getTestObject(String data) {
		JsFile r = new JsFile();
		r.rootElement = new JsElem();
		r.rootElement.children.add(new JsElem("data",data,true));
		return r;
	}
}
