package nl.zeesoft.zodb.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.DatabaseClientListener;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;

public class TestDatabaseClient extends TestObject implements DatabaseClientListener {
	public TestDatabaseClient(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDatabaseClient(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test can be used to test the ZODB *DatabaseClient*.");
	}
	
	@Override
	protected void test(String[] args) {
		Config config = new Config();
		config.setDebug(true);
		config.getZODB().url = "http://127.0.0.1:8080/ZODB/ZODB";
		
		DatabaseRequest request = new DatabaseRequest();
		request.type = DatabaseRequest.TYPE_LIST;
		request.max = 100;

		config.handleDatabaseRequest(request,this);
	}

	@Override
	public void handledRequest(DatabaseResponse res, ZStringBuilder err, Exception ex) {
		if (res!=null) {
			System.out.println("Results: " + res.results.size());
		}
		if (err.length()>0) {
			System.err.println("Error: " + err);
			if (ex!=null) {
				ex.printStackTrace();
			}
		}
		System.exit(0);
	}
}
