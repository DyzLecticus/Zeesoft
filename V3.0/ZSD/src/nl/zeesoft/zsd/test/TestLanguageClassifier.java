package nl.zeesoft.zsd.test;

import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zsd.SequenceClassifier;
import nl.zeesoft.zsd.interpret.InterpreterConfiguration;
import nl.zeesoft.zsd.sequence.AnalyzerSymbol;

public class TestLanguageClassifier extends TestSequenceClassifier {
	public TestLanguageClassifier(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestLanguageClassifier(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		System.out.println("This test uses a large JSON dataset to test a *SequenceClassifier* instance intended to be used as a language classifier.");
	}
	
	@Override
	protected void test(String[] args) {
		InterpreterConfiguration config = TestInterpreterConfiguration.getConfig(getTester());
		if (config==null) {
			System.out.println("This test has been skipped due to configuration initialization failure");
		} else {
			SequenceClassifier sc = config.getLanguageClassifier();
			
			assertEqual(sc.getLinkContextCounts().get(""),488891,"The total number of links does not match expectation");
			
			ZStringSymbolParser sequence = new ZStringSymbolParser("Wie ben jij?");
			testClassification(sc,sequence,false,"NLD");
			testClassification(sc,sequence,true,"NLD");
			sequence = new ZStringSymbolParser("what is name?");
			testClassification(sc,sequence,true,"ENG");
			sequence = new ZStringSymbolParser("twothousand");
			testClassification(sc,sequence,true,"ENG");

			List<AnalyzerSymbol> contexts = null;
			double t = 0D;
			sequence = new ZStringSymbolParser("wat is your name?");

			System.out.println();
			t = 0.3D;
			contexts = sc.getContexts(sequence,true,t);
			System.out.println("Context probabilities for '" + sequence + "', threshold: " + t);
			for (AnalyzerSymbol context: contexts) {
				System.out.println("'" + context.symbol + "': " + context.prob);
			}
			assertEqual(contexts.size(),2,"The classifier did not return the expected number of contexts");

			System.out.println();
			t = 0.7D;
			contexts = sc.getContexts(sequence,true,t);
			System.out.println("Context probabilities for '" + sequence + "', threshold: " + t);
			for (AnalyzerSymbol context: contexts) {
				System.out.println("'" + context.symbol + "': " + context.prob);
			}
			assertEqual(contexts.size(),1,"The classifier did not return the expected number of contexts");
		}
	}
}
