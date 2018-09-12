package nl.zeesoft.zevt.test;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zevt.ZEVTConfig;
import nl.zeesoft.zevt.mod.ModZEVT;
import nl.zeesoft.zevt.trans.TranslatorClientListener;
import nl.zeesoft.zevt.trans.TranslatorRequestResponse;

public class TestTranslatorClient extends TestObject implements TranslatorClientListener {
	public TestTranslatorClient(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestTranslatorClient(new Tester())).test(args);
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
		
		TranslatorRequestResponse request = new TranslatorRequestResponse();
		request.sequence = new ZStringSymbolParser("1 oktober");

		ModZEVT mod = (ModZEVT) config.getModule(ModZEVT.NAME);
		mod.url = "http://127.0.0.1:8080/ZEVT/ZEVT";
		mod.handleRequest(request,this);
	}

	@Override
	public void handledRequest(TranslatorRequestResponse res, ZStringBuilder err, Exception ex) {
		if (res!=null) {
			System.out.println("Entity value translation: " + res.translation);
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
