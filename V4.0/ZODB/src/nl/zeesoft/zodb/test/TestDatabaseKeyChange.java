package nl.zeesoft.zodb.test;

import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.Database;

public class TestDatabaseKeyChange extends TestObject {
	public TestDatabaseKeyChange(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDatabaseKeyChange(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test can be used to test the ZODB *Database* key change function.");
	}
	
	@Override
	protected void test(String[] args) {
		Config config = new Config();
		
		Database db = TestDatabase.initializeTestDatabase(config,new StringBuilder("0123012345678901234567890123456789012345678901234567890123456789"),true,30);
		
		if (db.isOpen()) {
			config.destroy();
		} else {
			System.exit(1);
		}
	}
}
