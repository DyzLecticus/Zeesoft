package nl.zeesoft.zodb.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.ClientListener;
import nl.zeesoft.zodb.db.DatabaseRequest;
import nl.zeesoft.zodb.db.DatabaseResponse;

public class TestClient extends TestObject implements ClientListener {
	public TestClient(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestClient(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test can be used to test the ZODB *Client*.");
	}
	
	@Override
	protected void test(String[] args) {
		Config config = new Config();
		config.initialize(true,"dist/","http://127.0.0.1:8080/ZODB",false);
		
		DatabaseRequest request = new DatabaseRequest();
		request.type = DatabaseRequest.TYPE_LIST;
		request.max = 100;

		sleep(1000);
		
		config.handleDatabaseRequest(request,this);
		
		sleep(3000);
	
		config.destroy();
	}

	@Override
	public void handledRequest(DatabaseResponse res, ZStringBuilder err, Exception e) {
		if (res!=null) {
			System.out.println("Results: " + res.results.size());
		}
		if (err.length()>0) {
			System.err.println("Error: " + err);
			if (e!=null) {
				e.printStackTrace();
			}
		}
	}
}
