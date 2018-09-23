package nl.zeesoft.zsc.test;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsc.confab.Confabulator;
import nl.zeesoft.zsc.confab.Context;
import nl.zeesoft.zsc.confab.CorrectionConfabulation;

public class TestConfabulator extends TestObject {
	public TestConfabulator(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestConfabulator(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/* TODO: describe
		System.out.println("This test shows how to convert a *DatabaseRequest* to and from JSON.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the database request");
		System.out.println("DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_LIST);");
		System.out.println("// Convert the database request to JSON");
		System.out.println("JsFile json = request.toJson();");
		System.out.println("// Convert the database request from JSON");
		System.out.println("request.fromJson(json);");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestConfabulator.class));
		System.out.println(" * " + getTester().getLinkForClass(DatabaseRequest.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the converted JSON.  ");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		Confabulator conf = (Confabulator) getTester().getMockedObject(MockConfabulator.class.getName());
		System.out.println("Confabulator name: " + conf.getName() + ", modules: " + conf.getModules());
		
		Context def = conf.getContext("");
		Context name = conf.getContext("Name");

		assertEqual(def.totalSymbols,40,"Total symbols for default context does not match expectation");
		assertEqual(def.totalLinks,122,"Total links for default context does not match expectation");
		assertEqual(name.totalSymbols,6,"Total symbols for 'Name' context does not match expectation");
		assertEqual(name.totalLinks,12,"Total links for 'Name' context does not match expectation");
		
		System.out.println("Symbols/links for default context: " + def.totalSymbols + "/" + def.totalLinks);
		System.out.println("Symbols/links for 'Name' context: " + name.totalSymbols + "/" + name.totalLinks);
		System.out.println("Symbol/link bandwidth for default context: " + def.symbolBandwidth + "/" + def.linkBandwidth);
		System.out.println("Symbol to link bandwidth factor for default context: " + def.symbolToLinkBandwidthFactor);
		System.out.println();

		testCorrection(conf,"My nam is Dyz agent.",true,"My name is Dyz Lecticus.");
		testCorrection(conf,"MY NAM IS DYZ AGENT.",true,"My name is Dyz Lecticus.");
		testCorrection(conf,"My goad is to help.",true,"My goal is to understand.");
		testCorrection(conf,"My goad is to help.",false,"My goal is to help.");
		testCorrection(conf,"gaad.",false,"Intelligent.");
	}
	
	private void testCorrection(Confabulator conf,String input,boolean validate,String expectedCorrection) {
		CorrectionConfabulation confab = new CorrectionConfabulation();
		confab.input.append(input);
		confab.validate = validate;
		confab.appendLog = true;
		conf.confabulate(confab);
		ZStringSymbolParser expected = new ZStringSymbolParser(expectedCorrection);
		assertEqual(confab.corrected,expected,"Correction does not match expectation");
		String val = "";
		if (validate) {
			val = " (validated links)";
		}
		System.out.println("Corrected: '" + confab.input + "' -> '" + confab.corrected + "'" + val);
		System.out.println("Log;");
		System.out.println(confab.log);
	}
}
