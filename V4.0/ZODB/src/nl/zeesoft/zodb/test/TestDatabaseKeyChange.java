package nl.zeesoft.zodb.test;

import java.io.File;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;
import nl.zeesoft.zodb.mod.ModZODB;

public class TestDatabaseKeyChange extends TestObject {
	public TestDatabaseKeyChange(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDatabaseKeyChange(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test can be used to test the ZODB *Database*.");
	}
	
	@Override
	protected void test(String[] args) {
		Config config = new Config();
		config.getModule(ModZODB.NAME).selfTest = false;
		config.setZODBKey(new StringBuilder("0123456789012345678901234567890123456789012345678901234567890123"));
		config.getZODB().setNewKey(new StringBuilder("0123012345678901234567890123456789012345678901234567890123456789"));
		config.initialize(true,"dist/","",false);
		
		File dir = new File("dist/");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		Database db = config.getZODB().getDatabase();
		db.install();

		sleep(1000);
		
		assertEqual(db.isOpen(),true,"Failed to initialize database within one second");
				
		sleep(1000);
		
		config.destroy();
	}
}
