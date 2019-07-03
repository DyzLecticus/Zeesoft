package nl.zeesoft.zodb.test;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.IndexElement;
import nl.zeesoft.zodb.db.idx.IndexConfig;
import nl.zeesoft.zodb.db.idx.SearchIndex;

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
			
			System.out.println("Started database ...");
			
			boolean hasIndex = true;
			IndexElement element = null;

			element = db.getObjectById(1L);
			if (element==null) {
				Date started = new Date();
				TestDatabase.addTestObjects(db);
				System.out.println("Adding 250 objects took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			}
			
			String name = "testObject:num";
			IndexConfig idxConf = db.getConfiguration().indexConfig;
			SearchIndex index = idxConf.getIndex(name);
			if (index!=null) {
				idxConf.removeIndex(name);
				System.out.println("Removed index " + name);
				hasIndex = false;
			} else {
				idxConf.addIndex("testObject","num",true,true);
				System.out.println("Added index " + name);
			}
			
			sleep(1000);
			
			System.out.println("Stopping database ...");
			
			db.stop();
			TestDatabase.waitForDatabaseState(db,false,10);

			System.out.println("Starting database ...");

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
}
