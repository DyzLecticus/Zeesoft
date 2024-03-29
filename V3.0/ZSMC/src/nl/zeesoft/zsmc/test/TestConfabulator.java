package nl.zeesoft.zsmc.test;

import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zsmc.confabulator.Confabulation;
import nl.zeesoft.zsmc.confabulator.Confabulator;
import nl.zeesoft.zsmc.confabulator.KnowledgeBases;

public class TestConfabulator extends TestObject {
	public TestConfabulator(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestConfabulator(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		/*
		System.out.println("This test shows how to use the *SymbolCorrector* to correct word spelling.");
		System.out.println();
		System.out.println("**Example implementation**  ");
		System.out.println("~~~~");
		System.out.println("// Create the SymbolCorrector");
		System.out.println("SpellChecker checker = new SymbolCorrector();");
		System.out.println("// Initialize the SymbolCorrector");
		System.out.println("checker.initialize(new ZStringSymbolParser(\"Some text containing correctly spelled words.\"));");
		System.out.println("// Use SymbolCorrector to correct a word");
		System.out.println("String correction = checker.correct(\"contaning\");");
		System.out.println("~~~~");
		System.out.println();
		System.out.println("Class references;  ");
		System.out.println(" * " + getTester().getLinkForClass(TestKnowledgeBases.class));
		System.out.println(" * " + getTester().getLinkForClass(SymbolCorrector.class));
		System.out.println();
		System.out.println("**Test output**  ");
		System.out.println("The output of this test shows the number of generated variations and corrections for certain words.");
		*/
	}
	
	@Override
	protected void test(String[] args) {
		KnowledgeBases kbs = (KnowledgeBases) getTester().getMockedObject(MockNLQnAKnowledgeBases.class.getName());
		assertEqual(kbs.getKnowledgeBases().size(),7,"The total number of forward knowledge bases does not match expectation");
		
		ZDKFactory factory = new ZDKFactory();
		Messenger msgr = factory.getMessenger();
		WorkerUnion uni = factory.getWorkerUnion(msgr);
		Confabulator conf = new Confabulator(msgr,uni,kbs);

		msgr.start();
		
		Confabulation confab = new Confabulation();
		
		confab.inputSymbols = new ZStringSymbolParser("Hoe an dat?");
		//confab.contextSymbols = new ZStringSymbolParser("nlPriveBetalen");
				
		conf.confabulate(confab);
		
		System.out.println(confab.log);
		
		/*
		ZStringSymbolParser starter = new ZStringSymbolParser("hoe kan dat?");
		conf.setConclusions(starter);
		List<String> conclusions = conf.getConclusions();
		ZStringBuilder result = getPrintConclusions(conclusions);
		System.out.println("Initial conclusions " + result);
		conf.startConfabulation(1000,10);
		while(conf.getFinalConclusions()==null) {
			sleep(1);
			conclusions = conf.getConclusions();
			result = getPrintConclusions(conclusions);
			System.out.println("Conclusions " + result);
			System.out.println("Active module symbols;");
			System.out.println(conf.getActiveSymbolsList());
		}
		conclusions = conf.getFinalConclusions();
		result = getPrintConclusions(conclusions);
		System.out.println("Final conclusions " + result);
		System.out.println("Active module symbols;");
		System.out.println(conf.getActiveSymbolsList());
		
		*/

		msgr.stop();
		uni.stopWorkers();
		msgr.whileWorking();
	}
	
	/*
	private ZStringBuilder getPrintConclusions(List<String> conclusions) {
		ZStringBuilder r = new ZStringBuilder();
		for (String symbol: conclusions) {
			if (r.length()>0) {
				r.append(" ");
			}
			if (symbol.length()==0) {
				r.append("[null]");
			} else {
				r.append(symbol);
			}
		}
		return r;
	}
	*/
}
