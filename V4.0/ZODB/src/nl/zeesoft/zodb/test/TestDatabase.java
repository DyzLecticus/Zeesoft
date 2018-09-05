package nl.zeesoft.zodb.test;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

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
		
		Database db = config.getZODB().getDatabase();
		db.install();

		sleep(1000);
		
		assertEqual(db.isOpen(),true,"Failed to initialize database within one second");
		if (db.isOpen()) {
			
			boolean added = false;
			IndexElement element = null;
			
			element = db.getObjectById(1L);
			if (element==null) {
				Date started = new Date();
				addTestObjects(db);
				System.out.println("Adding 250 objects took: " + ((new Date()).getTime() - started.getTime()) + " ms");
				added = true;
			}
			
			element = db.getObjectById(1L);
			if (element!=null) {
				assertEqual(element.name,"testObject001","Name of object found by id does not match expectation");
			} else {
				assertEqual(false,true,"Object with id 1 not found");
			}
			
			element = db.getObjectByName("testObject002");
			if (element!=null) {
				assertEqual(element.name,"testObject002","Name of object found by name does not match expectation");
			} else {
				assertEqual(false,true,"Object with name 'testObject002' not found");
			}
			
			sleep(1000);
			
			element = db.getObjectByName("testObject125");
			if (element!=null) {
				element = db.removeObject(element.id,null);
				if (element!=null) {
					assertEqual(element.name,"testObject125","Removed object name does not match expectation");
					element = addTestObject(db,"testObject125");
					assertEqual(element!=null,true,"Failed to add object with name 'testObject125'");
				}
			} else {
				element = addTestObject(db,"testObject125");
				assertEqual(element!=null,true,"Failed to add object with name 'testObject125'");
			}
			
			Date started = new Date();
			List<IndexElement> elements = db.getObjectsByNameStartsWith("testObject");
			assertEqual(elements.size(),250,"Number of objects does not match expectation");
			for (IndexElement elem: elements) {
				assertEqual(elem.obj!=null,true,"Failed to read object id " + elem.id);
			}
			long ms = ((new Date()).getTime() - started.getTime());
			if (!added) {
				System.out.println("Reading 250 objects took: " + ms + " ms");
			} else {
				System.out.println("Reading 250 cached objects took: " + ms + " ms");
			}

			SortedMap<String,Long> list = db.listObjects(100,100);
			assertEqual(list.size(),100,"List size does not match expectation");

			list = db.listObjects(200,100);
			assertEqual(list.size(),50,"List size does not match expectation");
			System.out.println("Obtained list size: " + list.size() + " (start: " + 200 + ", max: " + 100 + ")");
			System.out.println("First list object: " + list.firstKey());
			System.out.println("Last list object: " + list.lastKey());
		}
				
		sleep(1000);
		
		config.destroy();
	}
	
	private void addTestObjects(Database db) {
		for (int i = 1; i <= 250; i++) {
			IndexElement element = addTestObject(db,"testObject" + String.format("%03d",i));
			assertEqual((int) element.id,i,"Object id does not match expectation");
		}
	}

	private IndexElement addTestObject(Database db,String name) {
		return db.addObject(name,getTestObject(name),null);
	}
	
	private JsFile getTestObject(String data) {
		JsFile r = new JsFile();
		r.rootElement = new JsElem();
		r.rootElement.children.add(new JsElem("data",data,true));
		return r;
	}
}
