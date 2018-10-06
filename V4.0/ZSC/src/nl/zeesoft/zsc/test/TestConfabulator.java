package nl.zeesoft.zsc.test;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsc.confab.Confabulator;
import nl.zeesoft.zsc.confab.Context;
import nl.zeesoft.zsc.confab.confabs.ContextConfabulation;
import nl.zeesoft.zsc.confab.confabs.ContextResult;
import nl.zeesoft.zsc.confab.confabs.CorrectionConfabulation;
import nl.zeesoft.zsc.confab.confabs.ExtensionConfabulation;

public class TestConfabulator extends TestObject {
	public TestConfabulator(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestConfabulator(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test shows how to train a *Confabulator* to and use it to correct sequences and determine context.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the confabulator");
		System.out.println("DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_LIST);");
		System.out.println("// Train the confabulator");
		System.out.println("conf.learnSequence(\"A sequence to learn.\",\"OptionalContextSymbolToAssociate\");");
		System.out.println("conf.calculateProbabilities();");
		System.out.println("// Create a correction confabulation");
		System.out.println("CorrectionConfabulation confab1 = new CorrectionConfabulation();");
		System.out.println("confab1.input.append(\"A sequence to correct\");");
		System.out.println("// Confabulate the correction");
		System.out.println("conf.confabulate(confab1);");
		System.out.println("// Create a context confabulation");
		System.out.println("ContextConfabulation confab2 = new ContextConfabulation();");
		System.out.println("confab2.input.append(\"A sequence to determine context for\");");
		System.out.println("// Confabulate the context");
		System.out.println("conf.confabulate(confab2);");
		System.out.println("~~~~");
		System.out.println();
		getTester().describeMock(MockConfabulator.class.getName());
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestConfabulator.class));
		System.out.println(" * " + getTester().getLinkForClass(MockConfabulator.class));
		System.out.println(" * " + getTester().getLinkForClass(Confabulator.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows;  ");
		System.out.println(" * Some details about the trained confabulator  ");
		System.out.println(" * The result of some confabulations  ");
	}
	
	@Override
	protected void test(String[] args) {
		Confabulator conf = (Confabulator) getTester().getMockedObject(MockConfabulator.class.getName());
		System.out.println("Confabulator name: " + conf.getName() + ", max. distance: " + conf.getMaxDistance());
		
		Context def = conf.getContext("");
		Context name = conf.getContext("Name");

		assertEqual(def.totalSymbols,40,"Total symbols for default context does not match expectation");
		assertEqual(def.totalLinks,153,"Total links for default context does not match expectation");
		assertEqual(name.totalSymbols,6,"Total symbols for 'Name' context does not match expectation");
		assertEqual(name.totalLinks,14,"Total links for 'Name' context does not match expectation");
		
		System.out.println("Symbols/links for default context: " + def.totalSymbols + "/" + def.totalLinks);
		System.out.println("Symbols/links for 'Name' context: " + name.totalSymbols + "/" + name.totalLinks);
		System.out.println("Symbol/link bandwidth for default context: " + def.symbolBandwidth + "/" + def.linkBandwidth);
		System.out.println("Symbol to link bandwidth factor for default context: " + def.symbolToLinkBandwidthFactor);
		System.out.println();

		testCorrection(conf,"My nam is Dyz agent.",true,"My name is Dyz Lecticus.");
		testCorrection(conf,"MY NAM IS DYZ AGENT.",true,"My name is Dyz Lecticus.");
		testCorrection(conf,"My goad is to help.",true,"My goal is to understand.");
		testCorrection(conf,"My goad is to help.",false,"My goal is to help.");
		testCorrection(conf,"gaad.",false,"Gaad.");
		testCorrection(conf,"gaad.",false,"",0.1D);
		
		testContext(conf,"My name is Dyz Lecticus.",3);
		testContext(conf,"My name is Dyz Lecticus.",3,0.1D);
		testContext(conf,"I can learn context sensitive symbol sequences and use that knowledge to do things like correct symbols, classify context and more.",2);

		testExtension(conf,"I","",10,"can learn context sensitive symbol sequences and use that knowledge");
		testExtension(conf,"My","",5,"");
		testExtension(conf,"My","Name",5,"name is Dyz Lecticus.");
	}
	
	private void testCorrection(Confabulator conf,String input,boolean validate,String expectedCorrection) {
		testCorrection(conf,input,validate,expectedCorrection,0D);
	}

	private void testCorrection(Confabulator conf,String input,boolean validate,String expectedCorrection,double noise) {
		CorrectionConfabulation confab = new CorrectionConfabulation();
		confab.input.append(input);
		confab.validate = validate;
		confab.appendLog = true;
		confab.noise = noise;
		conf.confabulate(confab);
		if (expectedCorrection.length()>0) {
			ZStringSymbolParser expected = new ZStringSymbolParser(expectedCorrection);
			assertEqual(confab.corrected,expected,"Correction does not match expectation");
		}
		String val = "";
		if (validate) {
			val = " (validated links)";
		}
		System.out.println("Corrected: '" + confab.input + "' -> '" + confab.corrected + "'" + val);
		System.out.println("Log;");
		System.out.println(confab.log);
	}
	
	private void testContext(Confabulator conf,String input,int expectedContexts) {
		testContext(conf,input,expectedContexts,0D);
	}
	
	private void testContext(Confabulator conf,String input,int expectedContexts,double noise) {
		ContextConfabulation confab = new ContextConfabulation();
		confab.input.append(input);
		confab.noise = noise;
		conf.confabulate(confab);
		assertEqual(confab.results.size(),expectedContexts,"Context confabulation result size does not match expectation");
		System.out.println("Contexts for '" + confab.input + "': " + confab.results.size());
		for (ContextResult res: confab.results) {
			System.out.println(" - '" + res.contextSymbol + "' " + res.prob + "/" + res.probNormalized);
		}
		System.out.println();
	}
	
	private void testExtension(Confabulator conf,String input,String contextSymbol,int extend,String expectedExtension) {
		testExtension(conf,input,contextSymbol,extend,expectedExtension,0D);
	}
	
	private void testExtension(Confabulator conf,String input,String contextSymbol,int extend,String expectedExtension,double noise) {
		ExtensionConfabulation confab = new ExtensionConfabulation();
		confab.input.append(input);
		confab.contextSymbol = contextSymbol;
		confab.noise = noise;
		confab.extend = extend;
		confab.appendLog = true;
		conf.confabulate(confab);
		if (expectedExtension.length()>0) {
			assertEqual(confab.extension,new ZStringSymbolParser(expectedExtension),"Confabulated extension does not match expectation");
		}
		System.out.println("Extension for '" + confab.input + "': " + confab.extension);
		System.out.println("Log;");
		System.out.println(confab.log);
	}
}
