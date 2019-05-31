package nl.zeesoft.zodb.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZIntegerGenerator;
import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.IndexElement;
import nl.zeesoft.zodb.mod.ModZODB;

public class TestBigDatabase extends TestObject {
	public TestBigDatabase(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestBigDatabase(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test can be used to test the ZODB *Database*.");
	}
	
	@Override
	protected void test(String[] args) {
		Config config = new Config();
		
		Database db = TestDatabase.initializeTestDatabase(config,null,false);

		int i = 0;
		while(!db.isOpen()) {
			sleep(1000);
			if (i > 10) {
				break;
			}
		}
		assertEqual(db.isOpen(),true,"Failed to initialize database within " + i + " seconds");
		
		if (db.isOpen()) {
			IndexElement element = db.getObjectById(1L);
			if (element==null) {
				Date started = new Date();
				System.out.println("Adding objects ...");
				int added = addTestObjects(db);
				System.out.println("Adding " + added + " objects took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			} else {
				List<Integer> data = new ArrayList<Integer>();
				db.listObjects(0, 10, 0L,0L, data);
				System.out.println("Database has " + data.get(0) + " objects");
			}
		}
				
		sleep(1000);
		
		config.destroy();
	}
	
	private int addTestObjects(Database db) {
		int r = 0;
		for (int i = 1; i <= 250000; i++) {
			ZStringBuilder name = new ZStringBuilder(ModZODB.NAME + "/Objects/testObject");
			name.append(String.format("%06d",i));
			if (addTestObject(db,name)!=null) {
				r++;
			}
			if (r % 1000 == 0) {
				System.out.println("Added " + r + " objects");
			}
		}
		return r;
	}

	private IndexElement addTestObject(Database db,ZStringBuilder name) {
		return db.addObject(name,getTestObject(),null);
	}
	
	private JsFile getTestObject() {
		ZIntegerGenerator generator = new ZIntegerGenerator(4,16);
		ZStringBuilder data = new ZStringBuilder();
		int words = generator.getNewInteger();
		for (int w = 0; w < words; w++) {
			int letters = generator.getNewInteger();
			for (int l = 0; l < letters; l++) {
				data.append("" + l);
			}
			if (data.length()>0) {
				data.append(" ");
			}
		}
		JsFile r = new JsFile();
		r.rootElement = new JsElem();
		r.rootElement.children.add(new JsElem("testData",data,true));
		return r;
	}
}
