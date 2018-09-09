package nl.zeesoft.zevt.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zevt.ZEVTConfig;
import nl.zeesoft.zevt.app.AppZEVT;
import nl.zeesoft.zevt.trans.EntityClientListener;
import nl.zeesoft.zevt.trans.EntityRequestResponse;

public class TestEntityClient extends TestObject implements EntityClientListener {
	public TestEntityClient(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestEntityClient(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test can be used to test the ZODB *EntityClient*.");
	}
	
	@Override
	protected void test(String[] args) {
		ZEVTConfig config = new ZEVTConfig();
		config.setDebug(true);
		config.getZODB().url = "http://127.0.0.1:8080/ZEVT/ZODB";
		
		EntityRequestResponse request = new EntityRequestResponse();
		request.sequence = new ZStringSymbolParser("1 oktober");

		AppZEVT app = (AppZEVT) config.getApplication(AppZEVT.NAME);
		app.url = "http://127.0.0.1:8080/ZEVT/ZEVT";
		app.handleRequest(request,this);
	}

	@Override
	public void handledRequest(EntityRequestResponse res, ZStringBuilder err, Exception ex) {
		if (res!=null) {
			System.out.println("Entity value translation: " + res.entityValueTranslation);
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
