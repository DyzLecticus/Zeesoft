package nl.zeesoft.zodb.test;

import java.io.File;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.IndexElement;
import nl.zeesoft.zodb.db.idx.SearchIndex;
import nl.zeesoft.zodb.mod.ModZODB;

public class TestDatabaseRestart extends TestObject {
	public TestDatabaseRestart(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDatabaseRestart(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test can be used to test the ZODB *Database*.");
	}
	
	@Override
	protected void test(String[] args) {
		Config config = new Config();
		
		Database db = TestDatabase.initializeTestDatabase(config,null,true,10);

		if (db.isOpen()) {
			
			boolean hasIndex = true;
			IndexElement element = null;

			element = db.getObjectById(1L);
			if (element==null) {
				Date started = new Date();
				addTestObjects(db);
				System.out.println("Adding 250 objects took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			}
			
			String name = "testObject:num";
			SearchIndex index = db.getIndexConfig().getIndex(name);
			if (index!=null) {
				db.getIndexConfig().removeIndex(name);
				System.out.println("Removed index " + name);
				hasIndex = false;
			} else {
				db.getIndexConfig().addIndex("testObject","num",true,true);
				System.out.println("Added index " + name);
			}
			
			sleep(1000);
			
			db.stop();
			TestDatabase.waitForDatabaseState(db,false,10);
			
			db.start();
			TestDatabase.waitForDatabaseState(db,true,10);
			
			List<IndexElement> list = db.listObjects(100,100,0L,0L,null);
			assertEqual(list.size(),100,"List size does not match expectation (1)");

			if (hasIndex) {
				list = db.listObjectsUseIndex(0,300,true,name,true,DatabaseRequest.OP_GREATER,new ZStringBuilder("99"),0L,0L,null);
				assertEqual(list.size(),44,"List size does not match expectation (2)");
			}
			
			sleep(1000);
			
			config.destroy();
		} else {
			System.exit(1);
		}
	}
	
	public static Database initializeTestDatabase(Config config,StringBuilder newKey,boolean testIndexes,int timeOut) {
		File dir = new File("dist/data/ZODB/Index");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		config.getModule(ModZODB.NAME).selfTest = false;
		config.setZODBKey(new StringBuilder("0123456789012345678901234567890123456789012345678901234567890123"));
		if (newKey!=null && newKey.length()>0) {
			config.getZODB().setNewKey(newKey);
		}
		
		Database db = config.getZODB().getDatabase();
		if (testIndexes) {
			db.getIndexConfig().removeIndex(ModZODB.NAME + "/Objects/:testData");
			db.getIndexConfig().addIndex("testObject","data",false,true);
			db.getIndexConfig().getIndex("testObject:data").added = false;
			db.getIndexConfig().addIndex("testObject","num",true,true);
			db.getIndexConfig().getIndex("testObject:num").added = false;
			db.getIndexConfig().setRebuild(false);
		} else {
			db.getIndexConfig().getIndex(ModZODB.NAME + "/Objects/:testData").added = false;
		}

		config.initialize(true,"dist/","",false);
		
		db.install();
		
		int i = 0;
		while(!db.isOpen()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
			if (i >= timeOut) {
				break;
			}
		}
		if (!db.isOpen()) {
			System.out.println("Failed to initialize database within " + i + " seconds");
		}
		return db;
	}
	
	private void addTestObjects(Database db) {
		int num = -30;
		for (int i = 1; i <= 250; i++) {
			ZStringBuilder name = new ZStringBuilder("testObject");
			name.append(String.format("%03d",i));
			IndexElement element = addTestObject(db,name,num);
			assertEqual((int) element.id,i,"Object id does not match expectation");
			num += 3;
		}
	}

	private IndexElement addTestObject(Database db,ZStringBuilder name,int num) {
		return db.addObject(name,getTestObject(name,num),null);
	}
	
	private JsFile getTestObject(ZStringBuilder data,int num) {
		JsFile r = new JsFile();
		r.rootElement = new JsElem();
		r.rootElement.children.add(new JsElem("data",data,true));
		if (data.toString().equals("testObject125")) {
			num = 125;
		}
		r.rootElement.children.add(new JsElem("num","" + num));
		return r;
	}
}
