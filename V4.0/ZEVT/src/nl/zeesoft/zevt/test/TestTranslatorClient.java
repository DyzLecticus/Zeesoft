package nl.zeesoft.zevt.test;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsAbleClientRequest;
import nl.zeesoft.zdk.json.JsClientListener;
import nl.zeesoft.zdk.json.JsClientResponse;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zevt.ZEVTConfig;
import nl.zeesoft.zevt.mod.ModZEVT;
import nl.zeesoft.zevt.trans.TranslatorRequestResponse;
import nl.zeesoft.znlb.lang.Languages;

public class TestTranslatorClient extends TestObject implements JsClientListener {
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
		request.languages.add(Languages.NLD);
		request.sequence = new ZStringSymbolParser("1 oktober");

		ModZEVT mod = (ModZEVT) config.getModule(ModZEVT.NAME);
		mod.url = "http://127.0.0.1:8080/ZEVT/ZEVT";
		mod.handleRequest(request,this);
	}

	@Override
	public void handledRequest(JsClientResponse response) {
		if (response.response!=null) {
			TranslatorRequestResponse res = (TranslatorRequestResponse) ((JsAbleClientRequest) response.request).resObject;
			System.out.println("Entity value translation: " + res.translation);
		}
		if (response.error.length()>0) {
			System.err.println("Error: " + response.error);
			if (response.ex!=null) {
				response.ex.printStackTrace();
			}
		}
		System.exit(0);
	}
}
